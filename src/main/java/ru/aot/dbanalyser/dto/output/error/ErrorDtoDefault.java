package ru.aot.dbanalyser.dto.output.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ru.aot.dbanalyser.dto.output.DefaultResultDto;
import ru.aot.dbanalyser.enums.Mode;

@JsonPropertyOrder({"type", "message"})
public class ErrorDtoDefault extends DefaultResultDto {
    @JsonProperty("message")
    private final String message;

    public ErrorDtoDefault(String errorMessage) {
        super(Mode.ERROR);
        message = errorMessage;
    }

    public String getMessage() {
        return message;
    }
}
