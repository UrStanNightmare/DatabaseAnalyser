package ru.aot.dbanalyser.service.criteria;

import java.util.List;
import java.util.Set;

public interface DefaultEnumCriteria {
    Set<String> getRequiredCriteria();
    List<String> getCriteriaNames();

}
