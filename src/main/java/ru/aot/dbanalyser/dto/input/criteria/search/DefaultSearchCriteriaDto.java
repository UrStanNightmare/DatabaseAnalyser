package ru.aot.dbanalyser.dto.input.criteria.search;

import ru.aot.dbanalyser.dto.input.criteria.DefaultCriteria;
import ru.aot.dbanalyser.service.criteria.SearchCriteria;

public abstract class DefaultSearchCriteriaDto extends DefaultCriteria {
    private final SearchCriteria criteriaType;

    public DefaultSearchCriteriaDto(SearchCriteria criteriaType) {
        this.criteriaType = criteriaType;
    }

    public SearchCriteria getCriteriaType() {
        return criteriaType;
    }

    @Override
    public String toString() {
        return "DefaultSearchCriteriaDto{" +
                "criteriaType=" + criteriaType +
                '}';
    }
}
