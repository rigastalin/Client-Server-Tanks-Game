package edu.school21.servertanks.servertanks.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Points {
    private Integer numberClient;
    private Integer shot;
    private Integer hit;
}