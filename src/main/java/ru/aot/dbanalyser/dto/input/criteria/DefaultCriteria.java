package ru.aot.dbanalyser.dto.input.criteria;

import ru.aot.dbanalyser.service.criteria.DefaultEnumCriteria;

import java.util.Map;

public abstract class DefaultCriteria {
    public abstract String getSQLString();
    public abstract Map<String, String> getCriteriaParamsMap();
    public abstract DefaultEnumCriteria getCurrentCriteriaType();
}
