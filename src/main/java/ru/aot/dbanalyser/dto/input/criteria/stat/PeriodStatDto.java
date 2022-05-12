package ru.aot.dbanalyser.dto.input.criteria.stat;

import ru.aot.dbanalyser.service.criteria.DefaultEnumCriteria;
import ru.aot.dbanalyser.service.criteria.StatCriteria;

import java.util.Map;

public class PeriodStatDto extends DefaultStatCriteriaDto {
    private final String startDate;
    private final String endDate;

    public PeriodStatDto(String startDate, String endDate) {
        super(StatCriteria.PERIOD);
        this.startDate = startDate;
        this.endDate = endDate;

    }

    @Override
    public String toString() {
        return "PeriodStatDto{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }

    @Override
    public String getSQLString() {
        return  " SELECT customers.surname, customers.name, " +
                " goods.name AS prName, (SUM (goods.price) OVER ()) as totalSum, " +
                " (SUM(goods.price) OVER (PARTITION BY customers.Id, goods.name)) as customerGoodSum, " +
                " (SUM(goods.price) OVER (PARTITION BY customers.Id)) AS totalCustomerSum " +
                " FROM customers " +
                    " LEFT JOIN deals ON deals.buyerId = customers.Id " +
                    " LEFT JOIN goods ON goods.iD = deals.goodsId " +
                " WHERE " +
                " (deals.date::date >= '" + startDate + "' AND deals.date::date <= '" + endDate + "' " +
                " AND (SELECT EXTRACT (dow FROM deals.date)) != 0 AND (SELECT EXTRACT (dow FROM deals.date)) != 1) " +
                " OR deals.date IS NULL " +
                " ORDER BY customers.Id ASC, customergoodsum DESC;" +

                "SELECT DISTINCT " +
                " deals.date " +
                " FROM customers " +
                    " LEFT JOIN deals ON deals.buyerId = customers.Id " +
                    " LEFT JOIN goods ON goods.iD = deals.goodsId " +
                " WHERE " +
                " (deals.date::date >= '" + startDate + "' AND deals.date::date <= '" + endDate + "' " +
                " AND (SELECT EXTRACT (dow FROM deals.date)) != 0 AND (SELECT EXTRACT (dow FROM deals.date)) != 1) " +
                " OR deals.date IS NULL;";
    }

    @Override
    public Map<String, String> getCriteriaParamsMap() {
        return null;
    }

    @Override
    public DefaultEnumCriteria getCurrentCriteriaType() {
        return super.getCriteriaType();
    }
}
