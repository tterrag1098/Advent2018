package com.tterrag.advent2018.days;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;

import com.tterrag.advent2018.util.Day;

import lombok.Value;

public class Day06 extends Day {

    public static void main(String[] args) {
        new Day06().run();
    }
    
    @Value
    private static class Coordinate {
        int x, y;
        
        static Coordinate parse(String in) {
            String[] coords = in.split(", ");
            return new Coordinate(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
        }
    }

    private List<Coordinate> coords = parseList(Coordinate::parse);
    
    private int manhattanDistance(Coordinate coord, int x, int y) {
        return Math.abs(coord.getX() - x) + Math.abs(coord.getY() - y);
    }

    @Override
    protected Result doParts() {        
        int maxX = coords.stream().mapToInt(Coordinate::getX).max().getAsInt();
        int maxY = coords.stream().mapToInt(Coordinate::getY).max().getAsInt();
        
        Map<Coordinate, Integer> areas = new HashMap<>();
        
        Set<Coordinate> infiniteAreas = new HashSet<>();
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                final int fx = x;
                final int fy = y;
                List<Coordinate> byDistance = coords.stream().sorted(Comparator.comparingInt(c -> manhattanDistance(c, fx, fy))).collect(Collectors.toList());
                if (manhattanDistance(byDistance.get(0), x, y) == manhattanDistance(byDistance.get(1), x, y)) continue;
                if (x == 0 || y == 0 || x == maxX - 1 || y == maxY - 1) {
                    infiniteAreas.add(byDistance.get(0));
                }
                areas.merge(byDistance.get(0), 1, Integer::sum);
            }
        }
        
        for (Coordinate c : infiniteAreas) {
            areas.remove(c);
        }
        
        int within10k = 0;
        for (int x = -500; x <= maxX + 500; x++) {
            for (int y = -500; y <= maxY + 500; y++) {
                int sum = 0;
                for (Coordinate c : coords) {
                    sum += manhattanDistance(c, x, y);
                }
                if (sum < 10000) {
                    within10k++;
                }
            }
        }
        
        return new Result(areas.entrySet().stream().max(Entry.comparingByValue()).get().getValue().intValue(), within10k);
    }
}
