package com.mycompany.myteam.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseDto {
    private String message;
}
