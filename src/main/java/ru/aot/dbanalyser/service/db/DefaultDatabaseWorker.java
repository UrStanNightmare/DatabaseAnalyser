package ru.aot.dbanalyser.service.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aot.dbanalyser.dto.output.stat.StatCriteriaResultDto;
import ru.aot.dbanalyser.enums.Mode;
import ru.aot.dbanalyser.service.utils.PropertiesReader;
import ru.aot.dbanalyser.service.utils.StatHelper;

import java.sql.*;
import java.util.*;


/**
 * Класс для работы с запросами к бд и сбором информации. Синглтон.
 */
public class DefaultDatabaseWorker implements DatabaseWorker {
    private static final Logger log = LoggerFactory.getLogger(DefaultDatabaseWorker.class.getName());

    private static final String DB_DRIVER = "org.postgresql.Driver";
    private static final String URL_STRING = "db.url";
    private static final String USER_STRING = "db.user";
    private static final String PWD_STRING = "db.pwd";

    private static DefaultDatabaseWorker instance;

    private final String url;
    private final String username;
    private final String pwd;

    private DefaultDatabaseWorker(PropertiesReader propertiesReader) {
        this.url = propertiesReader.getProperty(URL_STRING);
        this.username = propertiesReader.getProperty(USER_STRING);
        this.pwd = propertiesReader.getProperty(PWD_STRING);
    }

    public static DefaultDatabaseWorker getInstance(PropertiesReader propertiesReader) {
        if (instance == null) {
            instance = new DefaultDatabaseWorker(propertiesReader);
            log.info("Сервис для работы с бд создан.");
        } else {
            log.warn("Сервис для работы с бд уже был создан ранее!");
        }
        return instance;
    }

    /**
     * Подключается к бд и возвращает соединение.
     * @return соединение с бд.
     * @throws ClassNotFoundException в случае отсутствия необходимого драйвера.
     * @throws SQLException в случае ошибки с соединением.
     */
    private Connection getDbConnection() throws ClassNotFoundException, SQLException {
        Connection dbConnection;
        try {
            Class.forName(DB_DRIVER);
            dbConnection = DriverManager.getConnection(url, username, pwd);

            return dbConnection;
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Не найден необходимый для работы с бд драйвер!!!");
        } catch (SQLException ex) {
            throw new SQLException("Не удалось установить соединение с бд", ex);
        }
    }

    /**
     * Выполняет запрос к бд и возвращает собранне данные в виде списка.
     * @param sqlQuery sql запрос
     * @param mode режим работы приложения
     * @return список полученных данных, собранных необходимым для режима образом.
     * @throws ClassNotFoundException в случае отсутствия драйвера для соединения с текущей бд.
     * @throws SQLException в случае ошибок с запросами.
     */
    @Override
    public List<?> getDataFromSQLRequest(String sqlQuery, Mode mode) throws ClassNotFoundException, SQLException {
        List<Map.Entry<String, String>> result = new ArrayList<>();

        Connection dbConnection = getDbConnection();
        try(Statement statement = dbConnection.createStatement()){
            statement.execute(sqlQuery);

            performDataCollectionDependingOnMode(mode, statement, result);
        }

        dbConnection.close();

        log.info("Запрос к бд выполнен.");
        return result;
    }

    private void performDataCollectionDependingOnMode(Mode mode, Statement statement, List<?> dataList) throws SQLException {
        ResultSet rs = statement.getResultSet();
        switch (mode) {
            case SEARCH: {
                List<Map.Entry<String, String>> searchList = (List<Map.Entry<String, String>>) dataList;
                while (rs.next()) {
                    String surname = rs.getString("surname");
                    String name = rs.getString("name");

                    searchList.add(new AbstractMap.SimpleEntry<>(surname, name));
                }
                break;
            }
            case STAT: {
                List<StatCriteriaResultDto> searchList = (List<StatCriteriaResultDto>) dataList;
                StatCriteriaResultDto currentCriteriaDto = null;
                int totalExpenses = 0;


                while (rs.next()) {
                    String surname = rs.getString("surname");
                    String name = rs.getString("name");
                    String productName = rs.getString("prname");
                    int totalExpensesBuffer = rs.getInt("totalsum");
                    int customerGoodsSum = rs.getInt("customergoodsum");
                    int totalCustomerSum = rs.getInt("totalcustomersum");

                    String fullName = surname + " " + name;

                    if (!StatHelper.isCriteriaDtoExistsByName(fullName, searchList)) {
                        currentCriteriaDto = new StatCriteriaResultDto(fullName, totalCustomerSum, new ArrayList<>());

                        searchList.add(currentCriteriaDto);
                    }

                    if (currentCriteriaDto != null && productName != null) {

                        boolean isGoodExists = StatHelper.checkIfGoodExists(productName, currentCriteriaDto.getPurchases());

                        if (!isGoodExists) {
                            Map<String, String> goodData = new HashMap<>(2);
                            goodData.put("name", productName);
                            goodData.put("expenses", String.valueOf(customerGoodsSum));

                            currentCriteriaDto.getPurchases().add(goodData);
                        }

                    }

                    if (totalExpensesBuffer > totalExpenses) {
                        totalExpenses = totalExpensesBuffer;
                    }
                }


                int daysCounted = 0;
                rs = getMoreResults(statement);
                while (rs != null && rs.next()) {
                    String dateString = rs.getString("date");
                    if (dateString != null) {
                        daysCounted++;
                    }
                }

                int activePeople = StatHelper.getActivePeopleCount(searchList);

                StatCriteriaResultDto configDto = StatHelper.generateConfigStatCriteriaDto(daysCounted, totalExpenses, activePeople);

                searchList.add(configDto);

                break;
            }
            default:
        }

    }

    /**
     * Возвращает следующий результат запроса в случае мультизапроса
     * @param statement посредник между бд, с помощью которого был выполнен мультизапрос
     * @return набор данных из бд
     * @throws SQLException в случае отсутствия данных из бд
     */
    private ResultSet getMoreResults(Statement statement) throws SQLException {
        if (statement.getMoreResults()) {
            return statement.getResultSet();
        }
        return null;
    }


}
