package com.mycompany.myteam.controller;

import com.mycompany.myteam.dto.*;
import com.mycompany.myteam.exception.InvalidInputException;
import com.mycompany.myteam.model.WordCombo;
import com.mycompany.myteam.service.WordsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/six_letter_words/v1")
@AllArgsConstructor
public class SixLetterWordsController {

    private WordsService service;

    @PostMapping
    public ResponseEntity findCombos(@Valid @RequestBody SixLetterWordsRequestDto req) {
        log.info("Received a SixLetterWordsRequest for {} input words", req.getWords().size());
        List<WordCombo> combos = null;
        try {
            combos = service.findCombos(req.getWords(), 6);
        } catch (InvalidInputException e) {
            return new ResponseEntity<>(new BaseResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new SixLetterWordsResponseDto(combos), HttpStatus.OK);
    }

}
