package com.autofly.service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autofly.model.HotspotZone;
import com.autofly.model.LatLng;
import com.autofly.model.RouteRequest;
import com.autofly.model.RouteResponse;
import com.autofly.repository.dao.HotspotRepository;
import com.autofly.repository.model.Hotspot;
import com.autofly.util.MapsUtil;

@Component
public class RouteService {

    @Autowired
    private MapsUtil mapUtils;

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

        System.out.println("Graph Dump");
        System.out.println(autoFlyGraph.getGraph().toString());

        System.out.println("Source and Destination");
        System.out.println(sourceHotspot + "   " + destHotspot);


        System.out.println("Shortest path from source to destination:");
        GraphPath<Hotspot, DefaultWeightedEdge> shortest_path = DijkstraShortestPath.findPathBetween(autoFlyGraph.getGraph(), sourceHotspot, destHotspot);
        System.out.println(shortest_path);


        if(shortest_path != null) {
            List<DefaultWeightedEdge> edges =  shortest_path.getEdgeList();


            List<HotspotZone> routeZones = new ArrayList<>();

            List<Hotspot> routeHotspots = shortest_path.getVertexList();

            Map<DefaultWeightedEdge, HotspotZone> edgeToZones = autoFlyGraph.getEdgeToZones();
            HotspotZone currentZone = edgeToZones.get(edges.get(0));

            for (DefaultWeightedEdge edge: edges) {
                HotspotZone zone = edgeToZones.get(edge);
                if( currentZone != null  && zone != null && zone.getZoneId() != currentZone.getZoneId()){
                    currentZone = zone;
                    Hotspot hotspot = autoFlyGraph.getGraph().getEdgeSource(edge);
                    if(routeHotspots.indexOf(hotspot) != -1) {
                        routeHotspots.get(routeHotspots.indexOf(hotspot)).setDropLocation(true);
                    }
                }
            }

            List<LatLng> walkFromSource = new ArrayList<>();
            walkFromSource.add(request.getSource());
            walkFromSource.add(new LatLng(request.getSource().getLat(), request.getSource().getLng()));

            List<LatLng> walkToDestination = new ArrayList<>();
            walkToDestination.add(new LatLng(request.getDestination().getLat(), request.getDestination().getLng()));
            walkToDestination.add(request.getDestination());

            double fare   = edges.size() * 10;

            response.setRoute(routeHotspots);
            response.setWalkFromSource(walkFromSource);
            response.setWalkToDestination(walkToDestination);
            response.setFare(fare);
            response.setSuccess(true);
        }
        else {
            response.setRoute(new ArrayList<>());
            response.setWalkFromSource(new ArrayList<>());
            response.setWalkToDestination(new ArrayList<>());
        }

        return response;
    }

    private Hotspot findClosestHotspot(LatLng location) {

//        Map<HotspotZone, List<Integer>> zones = zoneInstance.getZones();
//
//        List<HotspotZone> distances = new ArrayList<>();
//        for(HotspotZone h : zones.keySet()) {
//
//            Hotspot hotspot = hotspotRepo.findById(h.getMedianHotspotId()).orElse(new Hotspot());
//            LatLng hotspotLocation = new LatLng(hotspot.getLat(), hotspot.getLng());
//            distances.add(new HotspotZone(h,new Long(mapUtils.getDriveDist(location, hotspotLocation)).intValue()));
//            System.out.println("hotspot");
//            System.out.println(hotspot);
//            System.out.println("hotspot to location");
//            System.out.println(location + "   "  +hotspotLocation);
//            System.out.println(new Long(mapUtils.getDriveDist(location, hotspotLocation)).intValue());
//        }
//        //Find the nearest zone based on median hotspot locations
//        HotspotZone minDistZone = distances.stream()
//                .sorted((h1, h2) -> h1.getDistanceFrom() - h2.getDistanceFrom())
//                .findFirst()
//                .orElse(null);
//
//        List<Integer> hotspotIdList = zones.get(minDistZone);

        long minDistance = Long.MAX_VALUE;

        Hotspot closestHotspot = null;
        List<Hotspot> hotspotList = hotspotRepo.findAll();


        for (Hotspot hotspot: hotspotList){
            long distance = mapUtils.getWalkDist(location, new LatLng(hotspot.getLat(), hotspot.getLng()));
            if(distance < minDistance) {
                minDistance = distance;
                closestHotspot = hotspot;
            }
        }
        return closestHotspot;
    }
}
