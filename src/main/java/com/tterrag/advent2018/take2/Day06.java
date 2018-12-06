package com.tterrag.advent2018.take2;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    
    private int maxX = coords.stream().mapToInt(Coordinate::getX).max().getAsInt();
    private int maxY = coords.stream().mapToInt(Coordinate::getY).max().getAsInt();
    
    private int manhattanDistance(Coordinate coord, int x, int y) {
        return Math.abs(coord.getX() - x) + Math.abs(coord.getY() - y);
    }
    
    @Override
    protected Object part1() {
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
        
        infiniteAreas.forEach(areas::remove);
        
        return Collections.max(areas.values());
    }

    @Override
    protected Object part2() {
        int within10k = 0;
        for (int x = 0; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                int sum = 0;
                for (Coordinate c : coords) {
                    sum += manhattanDistance(c, x, y);
                }
                if (sum < 10000) {
                    within10k++;
                }
            }
        }
        
        return within10k;
    }
}
