package ru.aot.dbanalyser.dto.input.criteria.search;

import ru.aot.dbanalyser.service.criteria.DefaultEnumCriteria;
import ru.aot.dbanalyser.service.criteria.SearchCriteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BadCustomersCriteriaDto extends DefaultSearchCriteriaDto {
    private final int badCustomers;

    public BadCustomersCriteriaDto(int badCustomers) {
        super(SearchCriteria.PASSIVE_CUSTOMERS);
        this.badCustomers = badCustomers;
    }

    @Override
    public String toString() {
        return "BadCustomersCriteriaDto{" +
                "badCustomers=" + badCustomers +
                '}';
    }

    @Override
    public String getSQLString() {
        return "SELECT customers.surname, customers.name, COUNT(deals.goodsId) as deals FROM customers " +
                    " LEFT JOIN deals ON customers.Id = deals.buyerId " +
                " GROUP BY customers.Id, customers.surname, customers.name " +
                " ORDER BY deals ASC, surname ASC " +
                " LIMIT " + badCustomers + ";";
    }

    @Override
    public Map<String, String> getCriteriaParamsMap() {
        List<String> paramList = this.getCriteriaType().getCriteriaNames();
        Map<String,String> criteriaMap = new HashMap<>(paramList.size());

        criteriaMap.put(paramList.get(0), String.valueOf(badCustomers));

        return criteriaMap;
    }

    @Override
    public DefaultEnumCriteria getCurrentCriteriaType() {
        return super.getCriteriaType();
    }
}
