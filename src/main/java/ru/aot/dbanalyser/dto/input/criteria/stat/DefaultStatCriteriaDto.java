package ru.aot.dbanalyser.dto.input.criteria.stat;

import ru.aot.dbanalyser.dto.input.criteria.DefaultCriteria;
import ru.aot.dbanalyser.service.criteria.StatCriteria;

public abstract class DefaultStatCriteriaDto extends DefaultCriteria {
    private final StatCriteria criteriaType;

    public DefaultStatCriteriaDto(StatCriteria criteriaType) {
        this.criteriaType = criteriaType;
    }

    public StatCriteria getCriteriaType() {
        return criteriaType;
    }

    @Override
    public String toString() {
        return "DefaultStatCriteriaDto{" +
                "criteriaType=" + criteriaType +
                '}';
    }
}
