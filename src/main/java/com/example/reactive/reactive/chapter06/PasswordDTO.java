package com.example.reactive.reactive.chapter06;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordDTO {

    private String raw;
    private String secured;
}
