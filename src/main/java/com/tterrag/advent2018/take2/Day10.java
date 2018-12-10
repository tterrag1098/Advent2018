package com.tterrag.advent2018.take2;

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
        Rect bounds =  getBounds(points);
        int seconds = 0;
        while (bounds.getY2() - bounds.getY1() > 10) {
            for (int i = 0; i < points.size(); i++) {
                points.set(i, points.get(i).update());
            }
            bounds = getBounds(points);
            seconds++;
        }
        
        StringBuilder sb = new StringBuilder("\n");
        for (int y = bounds.getY1(); y <= bounds.getY2(); y++) {
            x: for (int x = bounds.getX1(); x <= bounds.getX2(); x++) {
                for (Point p : points) {
                    if (p.getX() == x && p.getY() == y) {
                        sb.append('#');
                        continue x;
                    }
                }
                sb.append('.');
            }
            sb.append('\n');
        }
        
        return new Result(sb.toString(), seconds);
    }
}
