package com.mycompany.myteam.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SixLetterWordsRequestDto {

    @NotNull(message = "A list of words is mandatory")
    private List<String> words;

}
