package com.alcorross;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DictionaryTest {

    private static final Dictionary dict = new Dictionary();
    private static final List<String> testList = new ArrayList<>();

    @BeforeAll
    static void loadDictionaryTest() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("src/test/resources/dictionaryTest.txt"))) {
            while ((line = br.readLine()) != null) testList.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void listDictionaryShouldBeEqualTestList() {
        dict.readDictionary();
        assertEquals(testList, Dictionary.getDictionary(), "The dictionary list must be equal " +
                "to the testList list");
    }

    @RepeatedTest(10000)
    void wordChoiceShouldReturnStringContainedInTheTestList() {
        String word = dict.wordChoice();
        assertTrue(testList.contains(word), "The wordChoice should return the word contained" +
                " in the testList: " + word);
    }
}
