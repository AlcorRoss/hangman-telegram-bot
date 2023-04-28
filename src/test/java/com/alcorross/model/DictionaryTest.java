package com.alcorross.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DictionaryTest {

    private static final Dictionary DICT = Dictionary.getInstance();
    private static final List<String> TEST_LIST = new ArrayList<>();

    @BeforeAll
    static void loadDictionaryTest() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader("src/test/resources/dictionaryTest.txt"))) {
            while ((line = br.readLine()) != null) TEST_LIST.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getDictionaryInstanceShouldReturnDictionary() {
        assertThat(Dictionary.getInstance())
                .withFailMessage("getDictionaryInstance() should return Dictionary")
                .isInstanceOf(Dictionary.class);
    }

    @RepeatedTest(10000)
    void wordChoiceShouldReturnStringContainedInTheTestList() {
        String word = DICT.wordChoice();
        assertThat(TEST_LIST).withFailMessage("The wordChoice should return the word contained" +
                                              " in the testList: " + word).contains(word);
    }
}
