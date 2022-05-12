package ru.aot.dbanalyser.service;

import ru.aot.dbanalyser.dto.output.DefaultResultDto;

import java.io.IOException;

public interface DbAnalyser {
    DefaultResultDto performAnalysis() throws IOException;
}
