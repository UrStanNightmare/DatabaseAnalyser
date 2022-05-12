package ru.aot.dbanalyser.dto.input.criteria.search;

import ru.aot.dbanalyser.service.criteria.DefaultEnumCriteria;
import ru.aot.dbanalyser.service.criteria.SearchCriteria;

import java.util.HashMap;
import java.util.Map;

public class LastNameCriteriaDto extends DefaultSearchCriteriaDto {
    private final String lastName;

    public LastNameCriteriaDto(String lastName) {
        super(SearchCriteria.LAST_NAME);
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "LastNameCriteriaDto{" +
                "lastName='" + lastName + '\'' +
                '}';
    }


    @Override
    public String getSQLString() {
        return "SELECT surname,name FROM customers WHERE surname='" + lastName + "' ORDER BY surname ASC, name ASC;";
    }

    @Override
    public Map<String, String> getCriteriaParamsMap() {
        Map<String, String> paramMap = new HashMap<>();

        String criteriaParamKey = this.getCriteriaType().getCriteriaNames().get(0);

        paramMap.put(criteriaParamKey, lastName);

        return paramMap;
    }

    @Override
    public DefaultEnumCriteria getCurrentCriteriaType() {
        return super.getCriteriaType();
    }
}
