package com.tterrag.advent2018.days;

import java.io.BufferedReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.tterrag.advent2018.util.Day;

import lombok.ToString;
import lombok.Value;

public class Day17 extends Day {

    public static void main(String[] args) {
        new Day17().run();
    }
    
    @Override
    protected Stream<String> inputStream(BufferedReader r) {
        return ("x=499, y=2..7\r\n" + 
                "y=7, x=495..501\r\n" + 
                "x=501, y=3..7\r\n" + 
                "x=498, y=2..4\r\n" + 
                "x=506, y=1..2\r\n" + 
                "x=498, y=10..13\r\n" + 
                "x=504, y=10..13\r\n" + 
                "y=13, x=498..504").lines();
    }
    
    @Value
    private static class Clay {
        int minX, maxX, minY, maxY;
        
        private static final Pattern X = Pattern.compile("x=(\\d+)(?:\\.\\.(\\d+))?");
        private static final Pattern Y = Pattern.compile("y=(\\d+)(?:\\.\\.(\\d+))?");

        static Clay parse(String line) {
            Matcher m = X.matcher(line);
            m.find();
            int minX = Integer.parseInt(m.group(1));
            int maxX = m.group(2) != null ? Integer.parseInt(m.group(2)) : minX;
            m = Y.matcher(line);
            m.find();
            int minY = Integer.parseInt(m.group(1));
            int maxY = m.group(2) != null ? Integer.parseInt(m.group(2)) : minY;
            return new Clay(minX, maxX, minY, maxY);
        }
    }
    
    @Value
    @ToString(includeFieldNames = false)
    private static class Node {
        int x, y;
        
        Node move(int x, int y) {
            return new Node(this.x + x, this.y + y);
        }
    }
    
    private boolean[][] clayMap;
    
    private boolean isClay(Node p) {
        return isClay(p.getX(), p.getY());
    }
    
    private boolean isClay(int x, int y) {
        if (x < 0 || x >= clayMap[0].length || y < 0 || y >= clayMap.length) {
            return false;
        }
        return clayMap[y][x];
    }
    
    private void printMap(int minX, Predicate<Node> flowing, Predicate<Node> filled) {
        for (int y = 0; y < clayMap.length; y++) {
            for (int x = minX - 1; x < clayMap[0].length; x++) {
                System.out.print(isClay(x, y) ? '#' : filled.test(new Node(x, y)) ? '~' : flowing.test(new Node(x, y)) ? '|' : '.');
            }
            System.out.println();
        }
        System.out.println();
    }
    
    @Override
    protected Result doParts() {
        List<Clay> input = parseList(Clay::parse);
        IntSummaryStatistics xStats = input.stream().mapToInt(Clay::getMaxX).summaryStatistics();
        IntSummaryStatistics yStats = input.stream().mapToInt(Clay::getMinY).summaryStatistics();
        
        clayMap = new boolean[yStats.getMax() + 1][xStats.getMax() + 2];
        for (Clay clay : input) {
            for (int x = clay.getMinX(); x <= clay.getMaxX(); x++) {
                for (int y = clay.getMinY(); y <= clay.getMaxY(); y++) {
                    clayMap[y][x] = true;
                }
            }
        }

        int minY = yStats.getMin();
        
        int springX = 500;
        
        Set<Node> flowed = new HashSet<>();
        Set<Node> filled = new HashSet<>();
        Deque<Node> flowing = new ArrayDeque<>();
        flowing.push(new Node(springX, minY));
        while (!flowing.isEmpty()) {
            printMap(xStats.getMin(), flowed::contains, filled::contains);
            Node head = flowing.pop();
            flowed.add(head);
            Node down = head.move(0, 1);
            Node left = head.move(-1, 0);
            Node right = head.move(1, 0);
            if (down.getY() > yStats.getMax()) {
                continue;
            }
            if (filled.contains(down) || isClay(down) || flowed.contains(down)) {
                if (isClay(down) || filled.contains(down)) {
                    if ((filled.contains(left) || isClay(left) || flowing.contains(left)) && (filled.contains(right) || isClay(right) || flowing.contains(right))) {
                        filled.add(head);
                    } else if ((!flowed.contains(left) || !flowed.contains(right))) {
                        flowing.push(head);
                        if (!filled.contains(left) && !isClay(left) && !flowing.contains(left)) {
                            flowing.push(left);
                        }
                        if (!filled.contains(right) && !isClay(right) && !flowing.contains(right)) {
                            flowing.push(right);
                        }
                    }
                }
            } else if (!flowing.contains(down)) {
                flowing.push(head);
                flowing.push(down);
            }
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        return new Result(flowed.size() + flowing.size(), "");
    }
}
