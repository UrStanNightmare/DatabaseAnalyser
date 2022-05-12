package ru.aot.dbanalyser.dto.output.search;

import ru.aot.dbanalyser.dto.output.DefaultCriteriaResultDto;
import ru.aot.dbanalyser.service.criteria.DefaultEnumCriteria;

import java.util.List;
import java.util.Map;

public class SearchCriteriaResultDto extends DefaultCriteriaResultDto {
    private final Map<String, String> criteriaList;
    private final List<?> resultsList;
    private final DefaultEnumCriteria currentCriteria;

    public SearchCriteriaResultDto(Map<String, String> criteriaList, List<?> resultsList, DefaultEnumCriteria currentCriteria) {
        this.criteriaList = criteriaList;
        this.resultsList = resultsList;
        this.currentCriteria = currentCriteria;
    }

    public Map<String, String> getCriteriaList() {
        return criteriaList;
    }

    public List<?> getResultsList() {
        return resultsList;
    }

    public DefaultEnumCriteria getCurrentCriteria() {
        return currentCriteria;
    }

    @Override
    public String toString() {
        return "SearchCriteriaResultDto{" +
                "criteriaList=" + criteriaList +
                ", resultsList=" + resultsList +
                ", currentCriteria=" + currentCriteria +
                '}';
    }
}
