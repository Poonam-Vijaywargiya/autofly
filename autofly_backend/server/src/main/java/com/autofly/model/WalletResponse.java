package com.autofly.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
@NoArgsConstructor
public class WalletResponse {
    private int passengerId;
    private double fare;
    private boolean isCreditAvailable;
}
