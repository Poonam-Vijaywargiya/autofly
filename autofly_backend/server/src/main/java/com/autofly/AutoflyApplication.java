package com.autofly;

import com.google.maps.GeoApiContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AutoflyApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoflyApplication.class, args);
    }

    @Bean("geoApiContext")
    public  GeoApiContext getGeoApiContext()
    {
        return new GeoApiContext.Builder()
                .apiKey("AIzaSyCUGRQwo2G7lPvEsVWxR_ten7F3pJvJxnA")
                .build();
    }
}
