package ru.aot.dbanalyser.dto.output.search;

import ru.aot.dbanalyser.dto.output.DefaultCriteriaResultDto;
import ru.aot.dbanalyser.dto.output.DefaultResultDto;
import ru.aot.dbanalyser.enums.Mode;

import java.util.List;

public class SearchResultDto extends DefaultResultDto {
    private final List<DefaultCriteriaResultDto> resultsList;

    //Не создаётся копия списка ради сохранения памяти
    public SearchResultDto(List<DefaultCriteriaResultDto> data) {
        super(Mode.SEARCH);
        this.resultsList = data;
    }

    public List<DefaultCriteriaResultDto> getResultsList() {
        return resultsList;
    }

    @Override
    public String toString() {
        return "SearchResultDto{" +
                "resultsList=" + resultsList +
                '}';
    }
}
