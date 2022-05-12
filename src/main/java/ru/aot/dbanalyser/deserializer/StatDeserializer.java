package ru.aot.dbanalyser.deserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.aot.dbanalyser.dto.input.InputFileDto;
import ru.aot.dbanalyser.dto.input.criteria.DefaultCriteria;
import ru.aot.dbanalyser.dto.input.criteria.stat.PeriodStatDto;
import ru.aot.dbanalyser.service.criteria.StatCriteria;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StatDeserializer extends StdDeserializer<InputFileDto> {
    private static final String START_DATE_KEY = "startDate";
    private static final String END_DATE_KEY = "endDate";

    public StatDeserializer() {
        this(null);
    }

    public StatDeserializer(Class<?> valueClass) {
        super(valueClass);
    }

    @Override
    public InputFileDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        List<DefaultCriteria> criteriaList = new ArrayList<>();

        JsonNode mainNode = jsonParser.getCodec().readTree(jsonParser);

        try {
            DefaultCriteria identified = identifyCriteria(mainNode);

            if (identified == null) {
                throw new JsonParseException(jsonParser, "Неизвестный критерий! " + mainNode.toString());
            }

            criteriaList.add(identified);
        } catch (IllegalArgumentException e) {
            throw new JsonParseException(jsonParser, e.getMessage());
        }

        return new InputFileDto(criteriaList);
    }

    private DefaultCriteria identifyCriteria(JsonNode criteria) throws IllegalArgumentException {
        Set<String> keysSet = generateKeySet(criteria);
        StatCriteria criteriaType = isCriteriaSetExists(keysSet);

        if (criteriaType != null) {
            return fillCriteria(criteriaType, criteria);
        }

        return null;
    }

    private StatCriteria isCriteriaSetExists(Set<String> checkSet) {
        for (StatCriteria criteria : StatCriteria.values()) {
            if (Objects.deepEquals(criteria.getRequiredCriteria(), checkSet)) {
                return criteria;
            }
        }

        return null;
    }

    private Set<String> generateKeySet(JsonNode criteria) {
        Set<String> keysSet = new HashSet<>();

        Iterator<String> keysIterator = criteria.fieldNames();

        while (keysIterator.hasNext()) {
            keysSet.add(keysIterator.next());
        }

        return keysSet;
    }

    private DefaultCriteria fillCriteria(StatCriteria criteriaType, JsonNode criteria) throws IllegalArgumentException {
        switch (criteriaType) {
            case PERIOD: {
                String startDate = criteria.get(START_DATE_KEY).textValue();
                String endDate = criteria.get(END_DATE_KEY).textValue();

                if (startDate == null || endDate == null || startDate.trim().isEmpty() || endDate.trim().isEmpty()) {
                    throw new IllegalArgumentException("Даты не должны быть пустыми!");
                }

                PeriodStatDto dto;
                try {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                    Date check = df.parse(startDate);
                    check = df.parse(endDate);

                    dto = new PeriodStatDto(startDate, endDate);
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Неверно указано для начальной или конечной даты!");
                }
                return dto;
            }
        }

        return null;
    }
}
