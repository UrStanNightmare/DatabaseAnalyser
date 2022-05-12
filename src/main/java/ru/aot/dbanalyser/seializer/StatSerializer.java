package ru.aot.dbanalyser.seializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.aot.dbanalyser.dto.output.stat.StatCriteriaResultDto;
import ru.aot.dbanalyser.dto.output.stat.StatResultDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StatSerializer extends StdSerializer<StatResultDto> {
    private static final String TYPE_STRING = "type";
    private static final String TYPE = "stat";
    private static final String TOT_DAYS_STRING = "totalDays";
    private static final String TOT_EXPENSES_STRING = "totalExpenses";
    private static final String AVG_EXPENSES_STRING = "avgExpenses";
    private static final String NAME_STRING = "name";
    private static final String PURCHASES_STRING = "purchases";
    private static final String EXPENSES_STRING = "expenses";

    public StatSerializer() {
        this(null);
    }

    public StatSerializer(Class<StatResultDto> t) {
        super(t);
    }

    @Override
    public void serialize(StatResultDto statResultDto, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        jsonGenerator.useDefaultPrettyPrinter();

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField(TYPE_STRING, TYPE);
        jsonGenerator.writeNumberField(TOT_DAYS_STRING, Integer.parseInt(statResultDto.getTotalDays()));

        jsonGenerator.writeArrayFieldStart("customers");

        //Список с каждым покупателем
        List<StatCriteriaResultDto> criteriaResultDtos = statResultDto.getCriteriaResultDtos();

        for (StatCriteriaResultDto customer : criteriaResultDtos) {
            jsonGenerator.writeStartObject();

            jsonGenerator.writeStringField(NAME_STRING, customer.getName());

            jsonGenerator.writeArrayFieldStart(PURCHASES_STRING);

            List<Map<String, String>> purchases = customer.getPurchases();

            for (Map<String, String> item : purchases) {
                jsonGenerator.writeStartObject();

                jsonGenerator.writeStringField(NAME_STRING, item.get(NAME_STRING));
                jsonGenerator.writeNumberField(EXPENSES_STRING, Integer.parseInt(item.get(EXPENSES_STRING)));

                jsonGenerator.writeEndObject();
            }

            jsonGenerator.writeEndArray();

            jsonGenerator.writeNumberField(TOT_EXPENSES_STRING, customer.getTotalExpenses());

            jsonGenerator.writeEndObject();
        }


        jsonGenerator.writeEndArray();

        jsonGenerator.writeNumberField(TOT_EXPENSES_STRING, Integer.parseInt(statResultDto.getTotalExpenses()));
        jsonGenerator.writeNumberField(AVG_EXPENSES_STRING, Double.parseDouble(statResultDto.getAvgExpenses()));

        jsonGenerator.writeEndObject();

    }
}
