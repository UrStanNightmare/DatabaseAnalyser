package ru.aot.dbanalyser.fileworkers;

import ru.aot.dbanalyser.dto.output.DefaultResultDto;

import java.io.IOException;

public interface OutputFileWriter{
    void writeFile(DefaultResultDto data) throws IOException;
}
