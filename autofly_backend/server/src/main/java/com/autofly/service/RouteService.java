package com.autofly.service;


import com.autofly.model.*;
import com.autofly.repository.dao.HotspotRepository;
import com.autofly.repository.model.Hotspot;
import com.autofly.util.MapsUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RouteService {

    @Autowired
    private MapsUtil mapUtils;

    @Autowired
    private FindHotspotZoneService findHotspotZoneService;

    @Autowired
    private AutoFlyGraph autoFlyGraph;

    private HotspotZones zoneInstance = HotspotZones.singleInstance;

    @Autowired
    private HotspotRepository hotspotRepo;


    public RouteResponse getRouteForPassenger(RouteRequest request) {

        RouteResponse response = new RouteResponse();


        response.setSource(request.getSource());
        response.setDestination(request.getDestination());
        response.setPassengerId(request.getPassengerId());
        response.setDepartureTime(request.getDepartureTime());

        Hotspot sourceHotspot = findClosestHotspot(request.getSource());
        Hotspot destHotspot = findClosestHotspot(request.getDestination());

//        System.out.println("Graph Dump");
//        System.out.println(autoFlyGraph.getGraph().toString());

        System.out.println("Source and Destination");
        System.out.println(sourceHotspot + "   " + destHotspot);


        System.out.println("Shortest path from source to destination:");
        GraphPath<Hotspot, DefaultWeightedEdge> shortest_path = DijkstraShortestPath.findPathBetween(autoFlyGraph.getGraph(), sourceHotspot, destHotspot);
        System.out.println(shortest_path);

        if(shortest_path != null) {
            response.setRoute(shortest_path.getVertexList());
            response.setSuccess(true);
        }

        return response;
    }

    private Hotspot findClosestHotspot(LatLng location) {

        Map<HotspotZone, List<Integer>> zones = zoneInstance.getZones();

        List<HotspotZone> distances = new ArrayList<>();
        for(HotspotZone h : zones.keySet()) {

            Hotspot hotspot = hotspotRepo.findById(h.getMedianHotspotId()).orElse(new Hotspot());
            LatLng hotspotLocation = new LatLng(hotspot.getLat(), hotspot.getLng());
            distances.add(new HotspotZone(h,new Long(mapUtils.getDriveDist(location, hotspotLocation)).intValue()));
        }
        //Find the nearest zone based on median hotspot locations
        HotspotZone minDistZone = distances.stream()
                .sorted((h1, h2) -> h1.getDistanceFrom() - h2.getDistanceFrom())
                .findFirst()
                .orElse(null);

        //Find which edge hotspot to assign to auto
        int edgeHotspotId1 = zones.get(minDistZone).get(0);
        int edgeHotspotId2 = zones.get(minDistZone).get(2);

        List<Integer> hotspotIdList = zones.get(minDistZone);

        long minDistance = Long.MAX_VALUE;

        Hotspot closestHotspot = null;

        for (Integer hotspotId: hotspotIdList){
            Hotspot hotspot = hotspotRepo.findById(hotspotId).orElse(new Hotspot());
            long distance = mapUtils.getDriveDist(location, new LatLng(hotspot.getLat(), hotspot.getLng()));

            if(distance < minDistance) {
                minDistance = distance;
                closestHotspot = hotspot;
            }
        }
        return closestHotspot;
    }
}
