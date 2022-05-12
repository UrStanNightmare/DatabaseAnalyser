package ru.aot.dbanalyser.dto.input.criteria.search;

import ru.aot.dbanalyser.service.criteria.DefaultEnumCriteria;
import ru.aot.dbanalyser.service.criteria.SearchCriteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductNameCriteriaDto extends DefaultSearchCriteriaDto {
    private final String productName;
    private final int minTimes;

    public ProductNameCriteriaDto(String productName, int minTimes) {
        super(SearchCriteria.PRODUCT_NAME);
        this.productName = productName;
        this.minTimes = minTimes;
    }

    @Override
    public String toString() {
        return "ProductNameCriteriaDto{" +
                "productName='" + productName + '\'' +
                ", minTimes=" + minTimes +
                '}';
    }

    @Override
    public String getSQLString() {
        return  "SELECT surname, name FROM customers" +
                    " LEFT JOIN ( " +
                        " SELECT deals.buyerId, deals.goodsId FROM deals " +
                        " WHERE goodsId = ( " +
                            " SELECT Id " +
                            " FROM goods " +
                            " WHERE name LIKE '"+ productName +"' " +
                            " LIMIT 1 " +
                        " )" +
                    " ) AS mtable ON mtable.buyerId = customers.Id " +
                " WHERE goodsId IS NOT NULL " +
                " GROUP BY(customers.Id) " +
                " HAVING COUNT(goodsId) >= "+ minTimes +";";
    }

    @Override
    public Map<String, String> getCriteriaParamsMap() {
        List<String> paramList = this.getCriteriaType().getCriteriaNames();
        Map<String,String> criteriaMap = new HashMap<>(paramList.size());

        criteriaMap.put(paramList.get(0), productName);
        criteriaMap.put(paramList.get(1), String.valueOf(minTimes));

        return criteriaMap;
    }

    @Override
    public DefaultEnumCriteria getCurrentCriteriaType() {
        return super.getCriteriaType();
    }
}
