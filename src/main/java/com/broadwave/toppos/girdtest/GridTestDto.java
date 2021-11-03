package com.broadwave.toppos.girdtest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GridTestDto {

    private String testName;
    private Integer testOld;
    private String testGender;
    private Integer testMoney;

}
