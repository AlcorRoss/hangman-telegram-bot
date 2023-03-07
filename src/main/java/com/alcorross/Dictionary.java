package com.alcorross;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dictionary {
    private final static List<String> dictionary = new ArrayList<>();

    public void readDictionary() {
        String line;
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("dictionary.txt");
        if (inputStream == null) throw new IllegalArgumentException("file not found! " + "dictionary.txt");
        try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream), 1384448)) {
            while ((line = bufReader.readLine()) != null) dictionary.add(line);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String wordChoice() {
        Random r = new Random();
        if (dictionary.isEmpty()) readDictionary();
        return dictionary.get(r.nextInt(0, dictionary.size()));
    }
}
