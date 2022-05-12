package ru.aot.dbanalyser.deserializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import ru.aot.dbanalyser.dto.input.InputFileDto;
import ru.aot.dbanalyser.dto.input.criteria.DefaultCriteria;
import ru.aot.dbanalyser.dto.input.criteria.search.BadCustomersCriteriaDto;
import ru.aot.dbanalyser.dto.input.criteria.search.LastNameCriteriaDto;
import ru.aot.dbanalyser.dto.input.criteria.search.MinMaxExpensesCriteriaDto;
import ru.aot.dbanalyser.dto.input.criteria.search.ProductNameCriteriaDto;
import ru.aot.dbanalyser.service.criteria.SearchCriteria;

import java.io.IOException;
import java.util.*;

public class SearchDeserializer extends StdDeserializer<InputFileDto> {
    private final static String CRITERIA_ARRAY_KEY_STRING = "criterias";
    private final static String LAST_NAME_KEY = "lastName";
    private final static String PRODUCT_NAME_KEY = "productName";
    private final static String MIN_GOOD_TIMES_KEY = "minTimes";
    private final static String MIN_EXPENSES_KEY = "minExpenses";
    private final static String MAX_EXPENSES_KEY = "maxExpenses";
    private final static String BAD_CUSTOMERS_KEY = "badCustomers";


    public SearchDeserializer() {
        this(null);
    }

    public SearchDeserializer(Class<?> valueClass) {
        super(valueClass);
    }

    @Override
    public InputFileDto deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        List<DefaultCriteria> criteriaList = new ArrayList<>();

        JsonNode mainNode = jsonParser.getCodec().readTree(jsonParser);
        JsonNode criteriaArray = mainNode.get(CRITERIA_ARRAY_KEY_STRING);

        if (criteriaArray == null){
            throw new JsonParseException(jsonParser, "Неверный формат входного файла!");
        }

        for (JsonNode criteria : criteriaArray) {
            try {
                DefaultCriteria identified = identifyCriteria(criteria);

                if (identified == null) {
                    throw new JsonParseException(jsonParser, "Неизвестный критерий! " + criteria.toString());
                }

                criteriaList.add(identified);
            } catch (IllegalArgumentException e) {
                throw new JsonParseException(jsonParser, e.getMessage());
            }
        }

        return new InputFileDto(criteriaList);
    }

    private DefaultCriteria identifyCriteria(JsonNode criteria) throws IllegalArgumentException {
        Set<String> keysSet = generateKeySet(criteria);
        SearchCriteria criteriaType = isCriteriaSetExists(keysSet);

        if (criteriaType != null) {
            return fillCriteria(criteriaType, criteria);
        }

        return null;
    }

    private SearchCriteria isCriteriaSetExists(Set<String> checkSet) {
        for (SearchCriteria criteria : SearchCriteria.values()) {
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

    private DefaultCriteria fillCriteria(SearchCriteria criteriaType, JsonNode criteria) throws IllegalArgumentException {
        switch (criteriaType) {
            case LAST_NAME: {
                String lastName = criteria.get(LAST_NAME_KEY).textValue();

                if (lastName == null || lastName.trim().isEmpty()) {
                    throw new IllegalArgumentException("Фамилия не указана!");
                }

                return new LastNameCriteriaDto(lastName);
            }
            case PRODUCT_NAME: {
                String productName = criteria.get(PRODUCT_NAME_KEY).textValue();
                int minTimes;
                if (criteria.get(MIN_GOOD_TIMES_KEY).isInt()) {
                    minTimes = criteria.get(MIN_GOOD_TIMES_KEY).intValue();
                } else {
                    throw new IllegalArgumentException(MIN_GOOD_TIMES_KEY + " должно быть числом!!!");
                }


                if (productName == null || productName.trim().isEmpty()) {
                    throw new IllegalArgumentException("Не указано название продукта!");
                }

                if (minTimes < 0) {
                    throw new IllegalArgumentException(MIN_GOOD_TIMES_KEY + " не должно быть меньше 0!");
                }

                return new ProductNameCriteriaDto(productName, minTimes);

            }

            case MIN_MAX_EXPENSES: {
                int minExpenses;
                int maxExpenses;

                if (criteria.get(MIN_EXPENSES_KEY).isInt()) {
                    minExpenses = criteria.get(MIN_EXPENSES_KEY).intValue();
                } else {
                    throw new IllegalArgumentException(MIN_EXPENSES_KEY + " должен быть числом!");
                }

                if (criteria.get(MAX_EXPENSES_KEY).isInt()) {
                    maxExpenses = criteria.get(MAX_EXPENSES_KEY).intValue();
                } else {
                    throw new IllegalArgumentException(MAX_EXPENSES_KEY + " должен быть числом!");
                }

                if (minExpenses < 0 || maxExpenses < 0) {
                    throw new IllegalArgumentException("Минимальная и максимальная стоимость не может быть меньше 0!");
                }

                return new MinMaxExpensesCriteriaDto(minExpenses, maxExpenses);

            }

            case PASSIVE_CUSTOMERS: {
                int badCustomers;

                if (criteria.get(BAD_CUSTOMERS_KEY).isInt()) {
                    badCustomers = criteria.get(BAD_CUSTOMERS_KEY).intValue();
                } else {
                    throw new IllegalArgumentException(BAD_CUSTOMERS_KEY + " должен быть числом!");
                }


                if (badCustomers < 1) {
                    throw new IllegalArgumentException("Количество покупателей не может быть меньше 1!");
                }

                return new BadCustomersCriteriaDto(badCustomers);
            }
        }

        return null;
    }
}
