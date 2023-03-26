package com.alcorross.model;

import com.alcorross.exceptions.DictionaryLoadException;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class Dictionary {
    private final List<String> DICTIONARY = new ArrayList<>();
    private static Dictionary dictionaryInstance;
    private static final Random RANDOM = new Random();

    private Dictionary() {
    }

    public static Dictionary getDictionaryInstance() {
        if (dictionaryInstance == null) dictionaryInstance = new Dictionary();
        return dictionaryInstance;
    }

    public void readDictionary() throws DictionaryLoadException {
        String line;
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("dictionary.txt")) {
            if (is == null) throw new NullPointerException("File not found: dictionary.txt");
            @Cleanup BufferedReader br = new BufferedReader(new InputStreamReader(is), 1384448);
            while ((line = br.readLine()) != null) DICTIONARY.add(line);
        } catch (NullPointerException | IOException e) {
            log.error("Failed to read file", e);
            throw new DictionaryLoadException("Failed to load dictionary");
        }
    }

    public String wordChoice() {
        return DICTIONARY.get(RANDOM.nextInt(0, DICTIONARY.size()));
    }
}