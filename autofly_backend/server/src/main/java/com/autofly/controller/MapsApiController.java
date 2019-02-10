package com.autofly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autofly.model.RouteRequest;
import com.autofly.util.MapsUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodingResult;

@RestController
@RequestMapping("/autofly/mapsApi")
public class MapsApiController {

    @Autowired
    private MapsUtil mapsUtil;

    @PostMapping("/findGeoCode")
    public ResponseEntity<GeocodingResult[]> getGeoCode(@RequestBody String currentAddress) {

        GeocodingResult[] results =  mapsUtil.getGeoCodeforAddress(currentAddress);

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping("/getDirections")
    public ResponseEntity<DirectionsResult> getDirections(@RequestBody RouteRequest routeRequest) {
    	
        DirectionsResult result =  mapsUtil.getDirections(routeRequest.getSource(), routeRequest.getDestination(), "DRIVING");

        if(result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/getDriveDistance")
    public ResponseEntity<Long> getDriveDistance(@RequestBody RouteRequest routeRequest) {
    	
       Long driveDist =  mapsUtil.getDriveDist(routeRequest.getSource(), routeRequest.getDestination());

        if(driveDist == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(driveDist, HttpStatus.OK);
    }
}
