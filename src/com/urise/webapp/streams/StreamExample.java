package com.urise.webapp.streams;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamExample {
    public static void main(String[] args) {
        int[] intArray = {2, 3, 3, 2, 3, 1}; // {2,3,3,2,3,1} {9,8}

        List<Integer> integers = Arrays.asList(2, 3, 3, 2, 3, 1);

        System.out.println("sum of array = " + integers.stream().mapToInt(integer -> integer).sum());

        System.out.println("minValue = " + minValue(intArray));

        System.out.println("oddOrEven = " + oddOrEven(integers));
    }


    // первое задание
    static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()     // отсеивает одинаковые
                .sorted()       // сортирует по ↑
                .reduce(0, (i, i2) -> i * 10 + i2);
    }

    // второе задание
    static List<Integer> oddOrEven(List<Integer> integers) {
        Map<Boolean, List<Integer>> map = integers.stream() // создаёт карту с двумя ключами:
                .collect(Collectors                         // true — нечётные, false — чётные
                        .partitioningBy(integer -> integer % 2 != 0));
        boolean isNumberOfOddsEven = map.get(true).size() % 2 == 0; // если число нечётных чётное, сумма будет чётная
        return isNumberOfOddsEven ? map.get(true) : map.get(false);
    }
}
