package ru.aot.dbanalyser.service.utils;

import ru.aot.dbanalyser.dto.output.stat.StatCriteriaResultDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Вспомогательный класс для проверки и анализа данных статистики.
 */
public class StatHelper {
    public static boolean isCriteriaDtoExistsByName(String name, List<StatCriteriaResultDto> list) {
        for (StatCriteriaResultDto dto : list) {
            if (dto.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkIfGoodExists(String name, List<Map<String, String>> data) {
        for (Map<String, String> map : data) {
            if (map.get("name").equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static int getActivePeopleCount(List<StatCriteriaResultDto> data){
        int active = 0;
        for (StatCriteriaResultDto criteria : data){
            if (criteria.getTotalExpenses() > 0){
                active++;
            }
        }
        return active;
    }

    /**
     * Создает сущность, хранящую более общую статистику для будушей передачи на уровень выше, распаковки и удаления
     * @param daysCounted число дней, за которые ведется статистика
     * @param totalExpenses суммарное количество потраченных денег
     * @param activePeople количество людей, которые тратили деньги
     * @return объект с данными для последующего взятия данных и удаления
     */
    public static StatCriteriaResultDto generateConfigStatCriteriaDto(int daysCounted, int totalExpenses, int activePeople){
        //Добавление финальной сущности для будущей распаковки более глобальных данных
        List<Map<String, String>> finalDataList = new ArrayList<>(1);
        Map<String, String> configMap = new HashMap<>(3);

        configMap.put("totalDays", String.valueOf(daysCounted));
        configMap.put("totalExpenses", String.valueOf(totalExpenses));

        double avgExpenses = 0;
        if (activePeople != 0){
            avgExpenses = (double) totalExpenses / (double) activePeople;
        }

        configMap.put("avgExpenses", String.valueOf(avgExpenses));

        finalDataList.add(configMap);

        return new StatCriteriaResultDto("", 0, finalDataList);
    }
}
