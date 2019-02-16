package com.autofly.model;


import com.autofly.repository.model.AutoDriver;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OffDutyResponse {
    private boolean success;
    private AutoDriver driver;
}
