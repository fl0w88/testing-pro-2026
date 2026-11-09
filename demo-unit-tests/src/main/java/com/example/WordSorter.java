package com.example;

import java.util.List;

public class WordSorter {

    public static List<String> sortWords(List<String> words) {
        words.sort(new StringComparator());
        return words;
    }

}
