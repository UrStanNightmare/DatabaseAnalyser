package ru.aot.dbanalyser.fileworkers;



import ru.aot.dbanalyser.dto.input.InputFileDto;

import java.io.IOException;
import java.nio.file.Path;

public interface InputFileReader {
    InputFileDto generateInputDto(Path filePath) throws IOException;
}
