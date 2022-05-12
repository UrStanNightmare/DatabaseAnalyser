package ru.aot.dbanalyser.dto.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.aot.dbanalyser.enums.Mode;

public abstract class DefaultResultDto {
    @JsonProperty("type")
    private Mode mode;

    public DefaultResultDto(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    @Override
    public String toString() {
        return "ResultDto{" +
                "mode=" + mode +
                '}';
    }
}
