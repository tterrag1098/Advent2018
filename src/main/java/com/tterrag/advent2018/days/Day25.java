package com.tterrag.advent2018.days;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Value;

import com.tterrag.advent2018.util.Day;

public class Day25 extends Day {

    public static void main(String[] args) {
        new Day25().run();
    }
    
    @Value
    private static class Point {
    	int x, y, z, t;
    	
    	int manhattanDistance(Point other) {
    		return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z) + Math.abs(t - other.t);
    	}
    	
    	static Point parse(String line) {
    		String[] coords = line.split(",");
    		return new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]), Integer.parseInt(coords[3]));
    	}
    }

    @Override
    protected Result doParts() {
    	LinkedList<Point> stars = parse(Point::parse).collect(Collectors.toCollection(LinkedList::new));
    	int constellations = 0;
    	while (!stars.isEmpty()) {
    		Point first = stars.removeFirst();
    		List<Point> constellation = new ArrayList<>();
    		constellation.add(first);
    		List<Point> inRange;
    		do {
    			inRange = constellation.stream().flatMap(p -> stars.stream().filter(s -> s.manhattanDistance(p) <= 3)).distinct().collect(Collectors.toList());
    			constellation.addAll(inRange);
    			stars.removeAll(inRange);
    		} while (!inRange.isEmpty());
    		constellations++;
    	}
    	
        return new Result(constellations, "");
    }
}
