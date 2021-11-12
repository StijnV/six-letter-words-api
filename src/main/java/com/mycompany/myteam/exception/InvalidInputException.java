package com.mycompany.myteam.exception;

import lombok.*;

@AllArgsConstructor
@Getter
public class InvalidInputException extends Exception {

    private final String message;

}
