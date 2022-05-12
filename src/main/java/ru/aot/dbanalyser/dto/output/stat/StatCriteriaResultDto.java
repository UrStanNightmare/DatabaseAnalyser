package ru.aot.dbanalyser.dto.output.stat;

import ru.aot.dbanalyser.dto.output.DefaultCriteriaResultDto;

import java.util.List;
import java.util.Map;

public class StatCriteriaResultDto extends DefaultCriteriaResultDto {
    private final String name;
    private final int totalExpenses;
    private final List<Map<String, String>> purchases;

    public StatCriteriaResultDto(String name, int totalExpenses, List<Map<String, String>> purchases) {
        this.name = name;
        this.totalExpenses = totalExpenses;
        this.purchases = purchases;
    }

    public String getName() {
        return name;
    }

    public int getTotalExpenses() {
        return totalExpenses;
    }

    public List<Map<String, String>> getPurchases() {
        return purchases;
    }

    @Override
    public String toString() {
        return "StatCriteriaResultDto{\n" +
                "name='" + name + '\'' +
                ", totalExpenses=" + totalExpenses +
                ", purchases=" + purchases.toString() +
                "}\n";
    }
}
