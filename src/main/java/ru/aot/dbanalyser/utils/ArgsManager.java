package ru.aot.dbanalyser.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aot.dbanalyser.dto.input.AppSettingsDto;
import ru.aot.dbanalyser.enums.Mode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Класс для работы с входными данными. Синглтон.
 */
public class ArgsManager {
    private static final Logger log = LoggerFactory.getLogger(ArgsManager.class.getName());
    private static final String WORKING_EXTENSION = "json";

    private static ArgsManager instance;

    private ArgsManager() {
    }

    public static ArgsManager getInstance() {
        if (instance == null) {
            instance = new ArgsManager();
        }
        return instance;
    }

    /**
     * Проверяет входные параметры командной строки.
     * @param args - аргументы командной строки
     * @throws IllegalArgumentException в случае, когда входные параметры указаны неверно.
     */
    public void checkArgs(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Неверное число аргументов!");
        }

        boolean mismatch = true;

        for (Mode mode : Mode.values()) {
            if (mode.equals(Mode.ERROR)){
                continue;
            }
            if (args[0].equals(mode.toString().toLowerCase())) {
                mismatch = false;
            }
        }

        if (mismatch) {
            throw new IllegalArgumentException("Неверно выбран тип операции!");
        }

        if (!args[1].endsWith(WORKING_EXTENSION)) {
            throw new IllegalArgumentException("Неверное расширение входного файла!");
        }

        if (!args[2].endsWith(WORKING_EXTENSION)) {
            throw new IllegalArgumentException("Неверное расширение выходного файла!");
        }

        Path openFilePath = Paths.get(args[1]);
        if (!Files.exists(openFilePath)) {
            throw new IllegalArgumentException("Не удаётся обнаружить входной файл!");
        }

        log.info("Все необходимые аргументы указаны.");
    }

    /**
     * Генерирует объект с настройками приложения
     * @param args пргументы командной строки
     * @return объект с настройками приложения
     */
    public AppSettingsDto generateSettings(String[] args) {
        Mode workingMode = Mode.valueOf(args[0].toUpperCase());
        Path inputFilePath = Paths.get(args[1]);
        Path outputFilePath = Paths.get(args[2]);

        return new AppSettingsDto(workingMode, inputFilePath, outputFilePath);
    }
}
