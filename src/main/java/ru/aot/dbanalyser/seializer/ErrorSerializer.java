package ru.aot.dbanalyser.seializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.aot.dbanalyser.dto.output.error.ErrorDtoDefault;

import java.io.IOException;

public class ErrorSerializer extends StdSerializer<ErrorDtoDefault> {
    private static final String TYPE_STRING = "type";
    private static final String ERROR_STRING = "error";
    private static final String MESSAGE_STRING = "message";

    public ErrorSerializer() {
        this(null);
    }

    public ErrorSerializer(Class<ErrorDtoDefault> t) {
        super(t);
    }

    @Override
    public void serialize(ErrorDtoDefault errorDtoDefault, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.useDefaultPrettyPrinter();

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField(TYPE_STRING, ERROR_STRING);
        jsonGenerator.writeStringField(MESSAGE_STRING, errorDtoDefault.getMessage());

        jsonGenerator.writeEndObject();
    }
}
