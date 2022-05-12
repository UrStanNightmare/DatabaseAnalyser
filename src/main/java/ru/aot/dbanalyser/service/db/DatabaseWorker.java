package ru.aot.dbanalyser.service.db;

import ru.aot.dbanalyser.enums.Mode;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseWorker {
    List<?> getDataFromSQLRequest(String sqlQuery, Mode mode) throws ClassNotFoundException, SQLException;

}
