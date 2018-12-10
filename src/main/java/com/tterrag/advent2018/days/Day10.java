package com.tterrag.advent2018.days;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tterrag.advent2018.util.Day;

import lombok.Value;
import lombok.experimental.Wither;

public class Day10 extends Day {

    public static void main(String[] args) {
        new Day10().run();
    }
    
    @Value
    private static class Point {
        int x, y;
        int velX, velY;
        
        Point update() {
            return new Point(x + velX, y + velY, velX, velY);
        }
        
        static Pattern PATTERN = Pattern.compile("position=<\\s*(-?\\d+),\\s*(-?\\d+)>\\s+velocity=<\\s*(-?\\d+),\\s*(-?\\d+)>");
        
        static Point parse(String line) {
            Matcher m = PATTERN.matcher(line);
            m.matches();
            return new Point(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)));
        }
    }
    
    @Value
    @Wither
    private static class Rect {
        int x1, y1, x2, y2;
    }
    
    private List<Point> points = parseList(Point::parse);
    
    private Rect getBounds(List<Point> points) {
        Rect ret = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (Point p : points) {
            if (p.getX() < ret.getX1()) {
                ret = ret.withX1(p.getX());
            } else if (p.getY() < ret.getY1()) {
                ret = ret.withY1(p.getY());
            } else if (p.getX() > ret.getX2()) {
                ret = ret.withX2(p.getX());
            } else if (p.getY() > ret.getY2()) {
                ret = ret.withY2(p.getY());
            }
        }
        return ret;
    }

    @Override
    protected Result doParts() {
        List<Point> prevPoints = new ArrayList<>(points);
        Rect prevBounds = new Rect(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        Rect bounds =  getBounds(points);
        int seconds = 0;
        while (seconds < 100000) {//bounds.getX1() >= prevBounds.getX1() && bounds.getY1() >= prevBounds.getY1() && bounds.getY2() <= prevBounds.getY2() && bounds.getX2() <= prevBounds.getX2()) {
            for (int i = 0; i < points.size(); i++) {
                points.set(i, points.get(i).update());
            }
            prevPoints = new ArrayList<>(points);
            prevBounds = bounds;
            bounds = getBounds(points);
            seconds++;
            
            if (bounds.getX2() - bounds.getX1() < 100 && bounds.getY2() - bounds.getY1() < 100) {
                for (int y = prevBounds.getY1(); y < prevBounds.getY2(); y++) {
                    x: for (int x = prevBounds.getX1(); x <= prevBounds.getX2(); x++) {
                        for (Point p : prevPoints) {
                            if (p.getX() == x && p.getY() == y) {
                                System.out.print('#');
                                continue x;
                            }
                        }
                        System.out.print('.');
                    }
                    System.out.println();
                }
                System.out.println(seconds);
            }
        }
        
        return new Result("", ""); // Result is in stdout
    }
}
