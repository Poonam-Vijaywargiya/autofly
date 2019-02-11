package com.autofly.model;

import java.time.LocalDateTime;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RouteRequest {
    private LatLng source;
    private LatLng destination;
    private int passengerId;
    private LocalDateTime departureTime;
    private LocalDateTime requestTimestamp = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteRequest that = (RouteRequest) o;
        return getPassengerId() == that.getPassengerId() &&
                Objects.equals(getSource(), that.getSource()) &&
                Objects.equals(getDestination(), that.getDestination()) &&
                Objects.equals(getDepartureTime(), that.getDepartureTime()) &&
                Objects.equals(requestTimestamp, that.requestTimestamp);
    }

    @Override
    public int hashCode() {

        return Objects.hash(getSource(), getDestination(),  getPassengerId(), getDepartureTime(), requestTimestamp);
    }

    @Override
    public String toString() {
        return "RouteRequest{" +
                "source=" + source +
                ", destination=" + destination +
                ", passengerId=" + passengerId +
                ", departureTime=" + departureTime +
                ", requestTimestamp=" + requestTimestamp +
                '}';
    }


}
