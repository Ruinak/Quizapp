package com.cos.quizapp.Dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForWordInfo {
    private int num;
    private String quiz;
    private ArrayList text;
}
