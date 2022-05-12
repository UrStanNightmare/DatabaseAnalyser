package ru.aot.dbanalyser.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aot.dbanalyser.dto.input.InputFileDto;
import ru.aot.dbanalyser.dto.input.criteria.DefaultCriteria;
import ru.aot.dbanalyser.dto.output.DefaultCriteriaResultDto;
import ru.aot.dbanalyser.dto.output.DefaultResultDto;
import ru.aot.dbanalyser.dto.output.search.SearchCriteriaResultDto;
import ru.aot.dbanalyser.dto.output.search.SearchResultDto;
import ru.aot.dbanalyser.dto.output.stat.StatCriteriaResultDto;
import ru.aot.dbanalyser.dto.output.stat.StatResultDto;
import ru.aot.dbanalyser.enums.Mode;
import ru.aot.dbanalyser.service.db.DatabaseWorker;
import ru.aot.dbanalyser.service.db.DefaultDatabaseWorker;
import ru.aot.dbanalyser.service.utils.PropertiesReader;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Стандартный анализатор бд. Синглтон.
 */
public class DefaultDbAnalyser implements DbAnalyser {
    private static final Logger log = LoggerFactory.getLogger(DefaultDbAnalyser.class.getName());

    private static DefaultDbAnalyser instance;
    private final Mode workingMode;
    private final InputFileDto inputFileDto;

    private DefaultDbAnalyser(Mode workingMode, InputFileDto inputFileDto) {
        this.workingMode = workingMode;
        this.inputFileDto = inputFileDto;
    }

    public static DefaultDbAnalyser getInstance(Mode workingMode, InputFileDto inputFileDto) {
        if (instance == null) {
            instance = new DefaultDbAnalyser(workingMode, inputFileDto);
            log.info("Сервис для анализа данных из бд создан.");
        } else {
            log.warn("Сервис для анализа данных из бд уже был создан ранее!");
        }
        return instance;
    }

    /**
     * Выполняет поиск необходимых данных в бд и генерирует объект с полученными данынми.
     * @return объект с полученными данными.
     * @throws IOException в случае невозможности доступа к бд или при ошибках при взаимодействии с бд.
     */
    public DefaultResultDto performAnalysis() throws IOException {
        List<DefaultCriteriaResultDto> resultDtoList = new ArrayList<>();
        DefaultResultDto finalResult = null;

        try {
            PropertiesReader propertiesReader = new PropertiesReader("dbprops.properties");

            DatabaseWorker dbWorker = DefaultDatabaseWorker.getInstance(propertiesReader);

            for (DefaultCriteria criteria : inputFileDto.getCriteriaList()) {
                List<?> result = dbWorker.getDataFromSQLRequest(criteria.getSQLString(), workingMode);
                DefaultCriteriaResultDto resultDto;

                if (workingMode == Mode.SEARCH) {
                    resultDto = new SearchCriteriaResultDto(criteria.getCriteriaParamsMap(), result, criteria.getCurrentCriteriaType());
                } else {
                    List<StatCriteriaResultDto> results = (List<StatCriteriaResultDto>) result;

                    StatCriteriaResultDto configDto = results.get(results.size() - 1);
                    results.remove(configDto);

                    List<Map<String, String>> configList = configDto.getPurchases();
                    Map<String, String> configMap = configList.get(0);

                    String totalDays = configMap.get("totalDays");
                    String totalExpenses = configMap.get("totalExpenses");
                    String avgExpenses = configMap.get("avgExpenses");

                    finalResult = new StatResultDto(totalDays, totalExpenses, avgExpenses, results);

                    return finalResult;
                }


                resultDtoList.add(resultDto);
            }

            if (workingMode == Mode.SEARCH) {
                finalResult = new SearchResultDto(resultDtoList);
            }


        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new IOException(e);
        }finally {
            log.info("Анализ завершён.");
        }

        return finalResult;
    }
}
