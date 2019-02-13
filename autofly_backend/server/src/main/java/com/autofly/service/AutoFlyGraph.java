package com.autofly.service;

import com.autofly.model.HotspotZone;
import com.autofly.model.LatLng;
import com.autofly.repository.dao.HotspotRepository;
import com.autofly.repository.model.Hotspot;
import com.autofly.util.MapsUtil;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AutoFlyGraph {

    private static Graph<Hotspot, DefaultWeightedEdge> graph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);

    private static Map<DefaultWeightedEdge, HotspotZone> edgeToZones = new HashMap<>();


    public AutoFlyGraph(HotspotRepository hotspotRepository, MapsUtil mapUtils) {
        List<Hotspot> hotspotList = hotspotRepository.findAll();

        for (Hotspot hotspot: hotspotList){
            graph.addVertex(hotspot);
        }

        Map<HotspotZone, List<Integer>> zones = HotspotZones.singleInstance.getZones();

        List<HotspotZone> distances = new ArrayList<>();
//        for(List<Integer> hotspots : zones.values()) {
        for(Map.Entry<HotspotZone, List<Integer>> zoneEntry: zones.entrySet()) {
            HotspotZone hotspotZone = zoneEntry.getKey();
            List<Integer> hotspots = zoneEntry.getValue();

            Hotspot h0 = hotspotRepository.findById(hotspots.get(0)).orElse(new Hotspot());
            Hotspot h1 = hotspotRepository.findById(hotspots.get(1)).orElse(new Hotspot());
            Hotspot h2 = hotspotRepository.findById(hotspots.get(2)).orElse(new Hotspot());

            if(!graph.containsEdge(h0, h1)) {
                DefaultWeightedEdge e1 =  graph.addEdge(h0,h1);
                graph.setEdgeWeight(e1, mapUtils.getDriveDist(new LatLng(h0.getLat(), h0.getLng()),new LatLng(h1.getLat(), h1.getLng())));
                edgeToZones.put(e1, hotspotZone);
            }

            if(!graph.containsEdge(h1, h2)) {
                DefaultWeightedEdge e2 = graph.addEdge(h1, h2);
                graph.setEdgeWeight(e2, mapUtils.getDriveDist(new LatLng(h1.getLat(), h1.getLng()), new LatLng(h2.getLat(), h2.getLng())));
                edgeToZones.put(e2, hotspotZone);
            }
        }

    }

    public Graph<Hotspot, DefaultWeightedEdge> getGraph(){
        return AutoFlyGraph.graph;
    }

    public Map<DefaultWeightedEdge, HotspotZone> getEdgeToZones(){
        return AutoFlyGraph.edgeToZones;
    }

}
