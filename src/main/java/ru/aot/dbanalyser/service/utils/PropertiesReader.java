package ru.aot.dbanalyser.service.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс для чтения конфигуграционных данных из файла.
 */
public class PropertiesReader {
    private final Properties properties;

    public PropertiesReader(String propertyFileName) throws IOException {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream(propertyFileName);
        this.properties = new Properties();
        this.properties.load(is);
    }

    /**
     * Возвращает конфигурационные данные по ключу
     * @param propertyName ключ для поиска данных
     * @return данные
     */
    public String getProperty(String propertyName) {
        String foundProperty = this.properties.getProperty(propertyName);
        if (foundProperty != null) {
            return foundProperty;
        } else {
            throw new IllegalArgumentException("Не найден параметр " + propertyName + " в properties файле dbprops!!!");
        }
    }
}
