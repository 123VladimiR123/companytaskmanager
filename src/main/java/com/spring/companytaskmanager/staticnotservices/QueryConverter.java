package com.spring.companytaskmanager.staticnotservices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QueryConverter {
    private static final String delimiters = "[ \"'.,:;]";
    private static final String[] type = new String[0];
    private static final Permutations perms = new Permutations();

    public static String convert (String input) {
        if (input == null || input.equals("") || input.equals("null")) return ".*";

        Set<String> set = Arrays.stream(input.toLowerCase().split(delimiters)).filter(e -> !e.isBlank()).collect(Collectors.toSet());

        if (set.size() == 0) return ".*";
        if (set.size() == 1) return ".*" + set.toArray()[0] + ".*";
        String[] array = set.toArray(type);
        if (set.size() > 4) return ".*" + String.join(".*", array) + ".*";

        return "'" + perms.getFromArray(array).stream().map(e -> String.join(".*", e))
                .collect(Collectors.joining("|")) + "'";
    }

    private static class Permutations {
        private List<String[]> getFromArray(String[] str) {
            List<String[]> list = new ArrayList<>();
            permuts(list, str, str.length - 1);
            return list;
        }

        private void permuts(List<String[]> result, String[] array, int index) {
            if (index == 0) result.add(array.clone());

            for (int i = 0; i <= index; i++) {
                permuts(result, array, index - 1);
                swap(array, (index % 2 == 0) ? 0 : i, index);
            }
        }

        private void swap(String[] array, int first, int second) {
            String temp = array[first];
            array[first] = array[second];
            array[second] = temp;
        }
    }
}
