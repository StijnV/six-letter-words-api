package com.mycompany.myteam.dto;

import com.mycompany.myteam.model.WordCombo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class SixLetterWordsResponseDto {

    private List<WordCombo> combos;//fixme?

}
