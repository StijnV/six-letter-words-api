package com.mycompany.myteam.model;

import lombok.Data;

import java.util.*;
import java.util.stream.*;

@Data
public class WordCombo {

    private ArrayList<String> partialWords = new ArrayList<>();
    private String completeWord = "";

    @Override
    public String toString() {
        return completeWord + "=" + partialWords.stream().collect(Collectors.joining("+"));
    }

    public String getPartialWord(){
        String result = "";
        for (String partialWord : partialWords) {
            result += partialWord;
        }
        return result;
    }

}
