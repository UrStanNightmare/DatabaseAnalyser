package ru.aot.dbanalyser.dto.input;

import ru.aot.dbanalyser.enums.Mode;

import java.nio.file.Path;

/**
 * Хранит базовые настройки приложения
 */
public class AppSettingsDto {
    private Mode workingMode;
    private Path openFilePath;
    private Path saveFilePath;

    /**
     * @param workingMode режим работы приложения
     * @param openFilePath путь к входному файлу
     * @param saveFilePath путь к выходному файлу
     */
    public AppSettingsDto(Mode workingMode, Path openFilePath, Path saveFilePath) {
        this.workingMode = workingMode;
        this.openFilePath = openFilePath;
        this.saveFilePath = saveFilePath;
    }

    public Mode getWorkingMode() {
        return workingMode;
    }

    public Path getOpenFilePath() {
        return openFilePath;
    }

    public Path getSaveFilePath() {
        return saveFilePath;
    }
}
