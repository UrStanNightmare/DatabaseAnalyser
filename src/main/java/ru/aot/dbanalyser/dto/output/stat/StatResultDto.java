package ru.aot.dbanalyser.dto.output.stat;

import ru.aot.dbanalyser.dto.output.DefaultResultDto;
import ru.aot.dbanalyser.enums.Mode;

import java.util.List;

public class StatResultDto extends DefaultResultDto {
    private final String totalDays;
    private final String totalExpenses;
    private final String avgExpenses;
    //customers
    private final List<StatCriteriaResultDto> criteriaResultDtos;


    public StatResultDto(String totalDays, String totalExpenses, String avgExpenses, List<StatCriteriaResultDto> criteriaResultDtos) {
        super(Mode.STAT);
        this.totalDays = totalDays;
        this.totalExpenses = totalExpenses;
        this.avgExpenses = avgExpenses;
        this.criteriaResultDtos = criteriaResultDtos;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public String getTotalExpenses() {
        return totalExpenses;
    }

    public String getAvgExpenses() {
        return avgExpenses;
    }

    public List<StatCriteriaResultDto> getCriteriaResultDtos() {
        return criteriaResultDtos;
    }

    @Override
    public String toString() {
        return "StatResultDto{" +
                "totalDays='" + totalDays + '\'' +
                ", totalExpenses='" + totalExpenses + '\'' +
                ", avgExpenses='" + avgExpenses + '\'' +
                ", criteriaResultDtos=" + criteriaResultDtos +
                '}';
    }
}
