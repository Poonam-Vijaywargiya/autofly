package com.autofly.service;


import com.autofly.model.OffDutyRequest;
import com.autofly.model.OffDutyResponse;
import com.autofly.model.StartRideRequest;
import com.autofly.model.StartRideResponse;
import com.autofly.repository.dao.AutoDriverRepository;
import com.autofly.repository.dao.HotspotRepository;
import com.autofly.repository.dao.PassengerRepository;
import com.autofly.repository.dao.RideRepository;
import com.autofly.repository.model.AutoDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OffDutyService {
    @Autowired
    private AutoDriverRepository driverRepo;

    @Autowired
    private RideRepository rideRepo;

    @Autowired
    private HotspotAutoQueue hotspotAutoQueue;

    @Autowired
    private HotspotRepository hotspotRepo;

    public OffDutyResponse offDuty(OffDutyRequest request) {
        OffDutyResponse response = new OffDutyResponse();

        AutoDriver driver = driverRepo.findByUserId(request.getDriverId());

        if(driver != null) {
          boolean isRemoved =   hotspotAutoQueue.removeAutoFromHotspot(driver);
          response.setDriver(driver);
          response.setSuccess(isRemoved);
        }
        return response;
    }
}
