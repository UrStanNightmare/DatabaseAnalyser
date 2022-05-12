package ru.aot.dbanalyser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.aot.dbanalyser.dto.input.AppSettingsDto;
import ru.aot.dbanalyser.dto.input.InputFileDto;
import ru.aot.dbanalyser.dto.output.DefaultResultDto;
import ru.aot.dbanalyser.dto.output.error.ErrorDtoDefault;
import ru.aot.dbanalyser.fileworkers.DefaultInputFileReader;
import ru.aot.dbanalyser.fileworkers.DefaultOutputFileWriter;
import ru.aot.dbanalyser.fileworkers.InputFileReader;
import ru.aot.dbanalyser.fileworkers.OutputFileWriter;
import ru.aot.dbanalyser.service.DbAnalyser;
import ru.aot.dbanalyser.service.DefaultDbAnalyser;
import ru.aot.dbanalyser.utils.ArgsManager;

import java.io.IOException;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class.getName());

    public static void main(String[] args) {
        log.info("Старт программы");

        AppSettingsDto appSettings;

        ArgsManager argsManager = ArgsManager.getInstance();

        OutputFileWriter fileWriter = null;

        try {
            argsManager.checkArgs(args);
            appSettings = argsManager.generateSettings(args);

            fileWriter = DefaultOutputFileWriter.getInstance(appSettings.getSaveFilePath());

            InputFileReader inputFileReader = DefaultInputFileReader.getInstance(appSettings.getWorkingMode());
            InputFileDto inputFileDto = inputFileReader.generateInputDto(appSettings.getOpenFilePath());

            DbAnalyser analyser = DefaultDbAnalyser.getInstance(appSettings.getWorkingMode(), inputFileDto);

            DefaultResultDto result = analyser.performAnalysis();
            fileWriter.writeFile(result);

        } catch (IllegalArgumentException | IOException e) {
            DefaultResultDto errorDto = new ErrorDtoDefault(e.getMessage());

            try {
                if (fileWriter != null) {
                    fileWriter.writeFile(errorDto);
                }
            } catch (IOException ex) {
                log.error("{}", ex.getMessage(), ex);
            }
            log.error("{}", e.getMessage(), e);
        }
    }
}
