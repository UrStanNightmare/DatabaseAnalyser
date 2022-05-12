package ru.aot.dbanalyser.dto.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.aot.dbanalyser.dto.input.criteria.DefaultCriteria;

import java.util.List;

public class InputFileDto {
    @JsonProperty("criterias")
    List<DefaultCriteria> criteriaList;

    public InputFileDto(List<DefaultCriteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    public List<DefaultCriteria> getCriteriaList() {
        return criteriaList;
    }

    @Override
    public String toString() {
        return "InputFileDto{" +
                "criteriaList=" + criteriaList.toString() +
                '}';
    }
}
