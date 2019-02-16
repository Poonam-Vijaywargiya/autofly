package com.autofly.model;


import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OffDutyRequest {
    private int driverId;
    private int currentHotspotId;
}
