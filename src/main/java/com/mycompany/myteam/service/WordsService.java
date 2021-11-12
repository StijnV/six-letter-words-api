package com.mycompany.myteam.service;

import com.mycompany.myteam.exception.InvalidInputException;
import com.mycompany.myteam.model.WordCombo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
@AllArgsConstructor
@Slf4j
public class WordsService {

    public List<WordCombo> findCombos(List<String> words, int maxWordLength) throws InvalidInputException {
        List<WordCombo> result = new ArrayList<>();

        List<String> validInputWords = words.stream()
                .filter(w -> w.length() <= maxWordLength)
                .collect(Collectors.toList());

        if(validInputWords.size() != words.size()){
            throw new InvalidInputException(String.format("There are words with more than %s letters in the input!", maxWordLength));
        }

        List<String> completeWordsThatWeWantToMach = validInputWords.stream()
                .filter(w -> w.length() == maxWordLength)
                .collect(Collectors.toList());

        List<String> partialWordsToCombine = validInputWords.stream()
                .filter(w -> w.length() < maxWordLength)
                .collect(Collectors.toList());

        for (String completeWord : completeWordsThatWeWantToMach) {
            System.out.println("##################################");
            WordCombo combo = new WordCombo();
            combo.setCompleteWord(completeWord);
            System.out.println("CompleteWord="+combo.getCompleteWord());


            for (int l = maxWordLength - 1; l > 0; l--) {
                for (int s = 0; s < maxWordLength; s++) {
//                    int startIndex = 0;
                    int startIndex = s;
                    for (int length = l; length > 0; length--) {
                        System.out.println("startIndex=" + startIndex);
                        if (startIndex + length > maxWordLength) {
                            System.out.println("length=" + length);
                            System.out.println("maxWordLength=" + maxWordLength);
                            System.out.println("startIndex + length > maxWordLength BREAK");
                            break;
                        }
                        String splitWord = completeWord.substring(startIndex, startIndex + length);
                        System.out.println("splitWord=" + splitWord);
                        if (partialWordsToCombine.contains(splitWord)) {
                            System.out.println("splitWord '" + splitWord + "' FOUND!");
                            combo.getPartialWords().add(splitWord);
                            if (completeWord.equals(combo.getPartialWord())) {
                                System.out.println("a word has been completed:" + combo.getCompleteWord());
                                result.add(combo);
                                break;
                            } else {
                                startIndex += splitWord.length();
                                System.out.println("new startIndex=" + startIndex);
                                length -= splitWord.length();
                                length++;
                                System.out.println("new length=" + length);
                                length++;
                            }
                        }
                    }
                }
            }
        }



        return result;
    }

}
