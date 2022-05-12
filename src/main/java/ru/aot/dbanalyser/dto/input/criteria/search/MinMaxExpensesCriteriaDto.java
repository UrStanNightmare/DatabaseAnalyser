package ru.aot.dbanalyser.dto.input.criteria.search;

import ru.aot.dbanalyser.service.criteria.DefaultEnumCriteria;
import ru.aot.dbanalyser.service.criteria.SearchCriteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinMaxExpensesCriteriaDto extends DefaultSearchCriteriaDto {
    private final int minExpenses;
    private final int maxExpenses;

    public MinMaxExpensesCriteriaDto(int minExpenses, int maxExpenses) {
        super(SearchCriteria.MIN_MAX_EXPENSES);
        this.minExpenses = minExpenses;
        this.maxExpenses = maxExpenses;
    }

    @Override
    public String toString() {
        return "MinMaxExpensesCriteriaDto{" +
                "minExpenses=" + minExpenses +
                ", maxExpenses=" + maxExpenses +
                '}';
    }

    @Override
    public String getSQLString() {
        return "SELECT customers.surname, customers.name FROM customers " +
                    " LEFT JOIN deals ON deals.buyerId = customers.Id " +
                    " LEFT JOIN goods ON deals.goodsId = goods.Id " +
                " WHERE deals.Id IS NOT NULL " +
                " GROUP BY customers.Id " +
                " HAVING SUM(goods.price) > "+ minExpenses +" AND SUM(goods.price) < "+ maxExpenses + ";";
    }

    @Override
    public Map<String, String> getCriteriaParamsMap() {
        List<String> paramList = this.getCriteriaType().getCriteriaNames();
        Map<String,String> criteriaMap = new HashMap<>(paramList.size());

        criteriaMap.put(paramList.get(0), String.valueOf(minExpenses));
        criteriaMap.put(paramList.get(1), String.valueOf(maxExpenses));

        return criteriaMap;
    }

    @Override
    public DefaultEnumCriteria getCurrentCriteriaType() {
        return super.getCriteriaType();
    }
}
