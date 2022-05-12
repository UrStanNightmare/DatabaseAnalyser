package ru.aot.dbanalyser.seializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ru.aot.dbanalyser.dto.output.DefaultCriteriaResultDto;
import ru.aot.dbanalyser.dto.output.search.SearchCriteriaResultDto;
import ru.aot.dbanalyser.dto.output.search.SearchResultDto;
import ru.aot.dbanalyser.service.criteria.DefaultEnumCriteria;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SearchSerializer extends StdSerializer<SearchResultDto> {
    private static final String TYPE_STRING = "type";
    private static final String TYPE = "search";
    private static final String RESULTS_STRING = "results";
    private static final String CRITERIA_STRING = "criteria";
    private static final String LAST_NAME_STRING = "lastName";
    private static final String FIRST_NAME_STRING = "firstName";

    public SearchSerializer() {
        this(null);
    }

    public SearchSerializer(Class<SearchResultDto> t) {
        super(t);
    }

    @Override
    public void serialize(SearchResultDto searchResultDto, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.useDefaultPrettyPrinter();

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField(TYPE_STRING, TYPE);

        List<DefaultCriteriaResultDto> criterias = searchResultDto.getResultsList();
        //Спиок критериев
        jsonGenerator.writeArrayFieldStart(RESULTS_STRING);
                for (DefaultCriteriaResultDto criteriaResult : criterias){
                    jsonGenerator.writeStartObject();

                    jsonGenerator.writeObjectFieldStart(CRITERIA_STRING);

                    Map<String, String> criteriaMapWithValues = ((SearchCriteriaResultDto) criteriaResult).getCriteriaList();

                    DefaultEnumCriteria currentCriteria = ((SearchCriteriaResultDto) criteriaResult).getCurrentCriteria();
                    List<String> criteriaNames = currentCriteria.getCriteriaNames();
                    for (String crName : criteriaNames){
                        jsonGenerator.writeStringField(crName, criteriaMapWithValues.get(crName));
                    }

                    jsonGenerator.writeEndObject();

                    List<Map.Entry<String, String>> resultsList = (List<Map.Entry<String, String>>) ((SearchCriteriaResultDto) criteriaResult).getResultsList();
                    jsonGenerator.writeArrayFieldStart(RESULTS_STRING);
                    for (Map.Entry<String, String> entry : resultsList){
                        jsonGenerator.writeStartObject();

                        jsonGenerator.writeStringField(LAST_NAME_STRING, entry.getKey());
                        jsonGenerator.writeStringField(FIRST_NAME_STRING, entry.getValue());

                        jsonGenerator.writeEndObject();
                    }

                    jsonGenerator.writeEndArray();

                    jsonGenerator.writeEndObject();
                }

        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();
    }
}
