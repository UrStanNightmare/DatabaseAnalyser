package ru.aot.dbanalyser.service.criteria;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Перечисление возможных критериев для режима поиска с указанием обязательных полей для выполнения запроса
 */
public enum SearchCriteria implements DefaultEnumCriteria {
    LAST_NAME("lastName"),
    PRODUCT_NAME("productName", "minTimes"),
    MIN_MAX_EXPENSES("minExpenses", "maxExpenses"),
    PASSIVE_CUSTOMERS("badCustomers");

    private final Set<String> requiredCriteria;
    private final List<String> criteriaNames;

    SearchCriteria(String... criteria) {
        this.requiredCriteria = new HashSet<>();
        this.criteriaNames = new ArrayList<>(criteria.length);

        for (String criteriaString : criteria) {
            this.requiredCriteria.add(criteriaString);
            this.criteriaNames.add(criteriaString);
        }
    }

    @Override
    public Set<String> getRequiredCriteria() {
        return requiredCriteria;
    }

    @Override
    public List<String> getCriteriaNames() {
        return criteriaNames;
    }
}
