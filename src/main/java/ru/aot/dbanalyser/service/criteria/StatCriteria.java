package ru.aot.dbanalyser.service.criteria;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Перечисление возможных критериев для режима статистики с указанием обязательных полей для выполнения запроса
 */
public enum StatCriteria implements DefaultEnumCriteria {
    PERIOD("startDate", "endDate");

    private final Set<String> requiredCriteria;

    StatCriteria(String... criteria) {
        this.requiredCriteria = new HashSet<>();
        for (String criteriaString : criteria) {
            this.requiredCriteria.add(criteriaString);
        }
    }

    @Override
    public Set<String> getRequiredCriteria() {
        return requiredCriteria;
    }

    @Override
    public List<String> getCriteriaNames() {
        return null;
    }
}
