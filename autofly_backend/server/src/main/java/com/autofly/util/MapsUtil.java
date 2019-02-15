package com.autofly.util;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autofly.model.LatLng;
import com.autofly.model.RouteRequest;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.TravelMode;

@Component
public class MapsUtil {

    @Autowired
    private GeoApiContext geoApiContext;

    public GeocodingResult[] getGeoCodeforAddress(String currentAddress) {

        GeocodingResult[] results = new GeocodingResult[0];

        String address = !currentAddress.isEmpty()? currentAddress : "Tower-C Prestige Shantiniketan, ITPL Main Road, Thigalarapalya, Krishnarajapura, Bengaluru, Karnataka";
        try {

            results = GeocodingApi.geocode(geoApiContext,address).await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//        System.out.println(gson.toJson(results[0].addressComponents));

        return results;
    }

    /***
     *
     * @param source @required
     * @param destination @required
     * @param travelMode Default travel mode is DRIVING
     * @return DirectionsResult[]
     */
    public DirectionsResult getDirections(LatLng source, LatLng destination, String travelMode) {

        DirectionsResult result = null;

        DirectionsApiRequest apiRequest = DirectionsApi.newRequest(geoApiContext);
        apiRequest.origin(new com.google.maps.model.LatLng(source.getLat(), source.getLng()));
        apiRequest.destination(new com.google.maps.model.LatLng(destination.getLat(), destination.getLng()));

        if("DRIVING".equalsIgnoreCase(travelMode) ){
            apiRequest.mode(TravelMode.DRIVING); //set travelling mode
        }
        else   if("WALKING".equalsIgnoreCase(travelMode) ){
            apiRequest.mode(TravelMode.WALKING); //set travelling mode
        }
        else   if("BICYCLING".equalsIgnoreCase(travelMode) ){
            apiRequest.mode(TravelMode.BICYCLING); //set travelling mode
        }
        else   if("TRANSIT".equalsIgnoreCase(travelMode) ){
            apiRequest.mode(TravelMode.TRANSIT); //set travelling mode
        }
        else {
            apiRequest.mode(TravelMode.DRIVING); // set default travelling mode
        }

        try {
            result = apiRequest.await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    return result;

    }

    /***
     *
     * @param source @required
     * @param destination @required
     * @param travelMode Provide travel mode
     * @param departureTime Provide departure time
     * @param through WayPoint locations
     * @return
     */
    public DirectionsResult getDirections(LatLng source, LatLng destination, String travelMode, LocalDateTime departureTime, LatLng through[]) {

        DirectionsResult result = null;

        DirectionsApiRequest apiRequest = DirectionsApi.newRequest(geoApiContext);
        apiRequest.origin(new com.google.maps.model.LatLng(source.getLat(), source.getLng()));
        apiRequest.destination(new com.google.maps.model.LatLng(destination.getLat(), destination.getLng()));

        if(departureTime != null){
            apiRequest.departureTime(departureTime.atZone(ZoneId.of("Asia/Kolkata")).toInstant());
        }
        else{
            apiRequest.departureTime(Instant.now());
        }

        if("DRIVING".equalsIgnoreCase(travelMode) ){
            apiRequest.mode(TravelMode.DRIVING); //set travelling mode
        }
        else   if("WALKING".equalsIgnoreCase(travelMode) ){
            apiRequest.mode(TravelMode.WALKING); //set travelling mode
        }
        else   if("BICYCLING".equalsIgnoreCase(travelMode) ){
            apiRequest.mode(TravelMode.BICYCLING); //set travelling mode
        }
        else   if("TRANSIT".equalsIgnoreCase(travelMode) ){
            apiRequest.mode(TravelMode.TRANSIT); //set travelling mode
        }
        else {
            apiRequest.mode(TravelMode.DRIVING); // set default travelling mode
        }

        if(through.length != 0) {
            com.google.maps.model.LatLng waypoints[] = new com.google.maps.model.LatLng[through.length];
            for (int i = 0; i < through.length; i++) {
                waypoints[i]  = new com.google.maps.model.LatLng(through[i].getLat(), through[i].getLng());
            }
            apiRequest.waypoints(waypoints);
        }

        try {
            result = apiRequest.await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    /***
     *
     * @param routeRequest Route Request from app, Default travel mode Driving is selected
     * @return
     */
    public DirectionsResult getDirections(RouteRequest routeRequest) {

        LatLng source = routeRequest.getSource();
        LatLng destination = routeRequest.getDestination();
        String travelMode = null;
        LocalDateTime departureTime = routeRequest.getDepartureTime();
        DirectionsResult result = null;

        DirectionsApiRequest apiRequest = DirectionsApi.newRequest(geoApiContext);
        apiRequest.origin(new com.google.maps.model.LatLng(source.getLat(), source.getLng()));
        apiRequest.destination(new com.google.maps.model.LatLng(destination.getLat(), destination.getLng()));

        if("DRIVING".equalsIgnoreCase(travelMode) ){
            apiRequest.mode(TravelMode.DRIVING); //set travelling mode
        }
        else   if("WALKING".equalsIgnoreCase(travelMode) ){
            apiRequest.mode(TravelMode.WALKING); //set travelling mode
        }
        else   if("BICYCLING".equalsIgnoreCase(travelMode) ){
            apiRequest.mode(TravelMode.BICYCLING); //set travelling mode
        }
        else   if("TRANSIT".equalsIgnoreCase(travelMode) ){
            apiRequest.mode(TravelMode.TRANSIT); //set travelling mode
        }
        else {
            apiRequest.mode(TravelMode.DRIVING); // set default travelling mode
        }
        if(departureTime != null){
            apiRequest.departureTime(departureTime.atZone(ZoneId.of("Asia/Kolkata")).toInstant());
        }

        try {
            result = apiRequest.await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    /***
     *
     * @param source @required as String
     * @param destination @required as String
     * @return Distance in Meters
     */
     public long getDriveDist(String source, String destination) {

         DistanceMatrixApiRequest apiRequest = DistanceMatrixApi.newRequest(geoApiContext);

         DistanceMatrix result  = null;
         try {
             result = apiRequest.origins(source)
                     .destinations(destination)
                     .mode(TravelMode.DRIVING)
                     .avoid(DirectionsApi.RouteRestriction.TOLLS)
                     .await();
         } catch (ApiException e) {
             e.printStackTrace();
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }

         long distanceinMeters = result.rows[0].elements[0].distance.inMeters;

         return  distanceinMeters;
     }

    /***
     *
     * @param source @required as com.autofly.model.LatLng
     * @param destination @required com.autofly.model.LatLng
     * @return Distance in Meters
     */
    public long getDriveDist(LatLng source, LatLng destination) {

        DistanceMatrixApiRequest apiRequest = DistanceMatrixApi.newRequest(geoApiContext);

        DistanceMatrix result  = null;
        try {
            result = apiRequest.origins(new com.google.maps.model.LatLng(source.getLat(), source.getLng()))
                    .destinations(new com.google.maps.model.LatLng(destination.getLat(), destination.getLng()))
                    .mode(TravelMode.DRIVING)
                    .avoid(DirectionsApi.RouteRestriction.TOLLS)
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(source);
        System.out.println(destination);
        System.out.println(result.rows[0]);
        System.out.println(result.rows[0].elements[0]);


        System.out.println(" ");

        long distanceinMeters = result.rows[0].elements[0].distance.inMeters;

        return  distanceinMeters;
    }

    /***
     *
     * @param source @required as com.autofly.model.LatLng
     * @param destination @required com.autofly.model.LatLng
     * @return Distance in Meters
     */
    public long getWalkDist(LatLng source, LatLng destination) {

        DistanceMatrixApiRequest apiRequest = DistanceMatrixApi.newRequest(geoApiContext);

        DistanceMatrix result  = null;
        try {
            result = apiRequest.origins(new com.google.maps.model.LatLng(source.getLat(), source.getLng()))
                    .destinations(new com.google.maps.model.LatLng(destination.getLat(), destination.getLng()))
                    .mode(TravelMode.WALKING)
                    .avoid(DirectionsApi.RouteRestriction.TOLLS)
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(source);
        System.out.println(destination);
        System.out.println(result.rows[0]);
        System.out.println(result.rows[0].elements[0]);


        System.out.println(" ");

        long distanceinMeters = result.rows[0].elements[0].distance.inMeters;

        return  distanceinMeters;
    }
       
}
