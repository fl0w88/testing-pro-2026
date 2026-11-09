package com.example;

import java.util.Comparator;

public class StringComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        // Übung 4b: compareTo unterscheidet Groß- und Kleinschreibung.
        // Laut Anforderung soll sie keine Rolle spielen, daher compareToIgnoreCase.
        // Zusatz: Für eine Sortierung wie im deutschen Wörterbuch (Ä neben A) eignet sich
        // java.text.Collator.getInstance(java.util.Locale.GERMAN).compare(o1, o2).
        return o1.compareToIgnoreCase(o2);
    }

}
