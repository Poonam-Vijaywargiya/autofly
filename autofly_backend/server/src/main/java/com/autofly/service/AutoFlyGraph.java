package com.autofly.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Component;

import com.autofly.model.HotspotZone;
import com.autofly.model.LatLng;
import com.autofly.repository.dao.HotspotRepository;
import com.autofly.repository.model.Hotspot;
import com.autofly.util.MapsUtil;

@Component
public class AutoFlyGraph {

    private static Graph<Hotspot, DefaultWeightedEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

    private static Map<DefaultWeightedEdge, HotspotZone> edgeToZones = new HashMap<>();

    public AutoFlyGraph(HotspotRepository hotspotRepository, MapsUtil mapUtils) {
        List<Hotspot> hotspotList = hotspotRepository.findAll();

        for (Hotspot hotspot: hotspotList){
            graph.addVertex(hotspot);
        }

        Map<HotspotZone, List<Integer>> zones = HotspotZones.singleInstance.getZones();

        List<HotspotZone> distances = new ArrayList<>();

        for(Map.Entry<HotspotZone, List<Integer>> zoneEntry: zones.entrySet()) {
            HotspotZone hotspotZone = zoneEntry.getKey();
            List<Integer> hotspots = zoneEntry.getValue();

            for (int i = 0; i < hotspots.size()-1; i++) {
                Hotspot hotspotI = hotspotRepository.findById(hotspots.get(i)).orElse(new Hotspot());
                Hotspot hotspotI1 = hotspotRepository.findById(hotspots.get(i + 1)).orElse(new Hotspot());

                if(!graph.containsEdge(hotspotI, hotspotI1)) {
                    DefaultWeightedEdge e1 =  graph.addEdge(hotspotI, hotspotI1);

                    graph.setEdgeWeight(e1, mapUtils.getDriveDist(new LatLng(hotspotI.getLat(), hotspotI.getLng()),new LatLng(hotspotI1.getLat(), hotspotI1.getLng())));
                    edgeToZones.put(e1, hotspotZone);
                }
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
