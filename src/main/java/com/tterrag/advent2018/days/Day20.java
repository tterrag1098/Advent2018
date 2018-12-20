package com.tterrag.advent2018.days;

import java.util.ArrayDeque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.tterrag.advent2018.util.Day;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;

public class Day20 extends Day {

    public static void main(String[] args) {
        new Day20().run();
    }
    
    @Value
    private static class Point {
        int x, y;
    }
    
    @Value
    @EqualsAndHashCode(of = {"x", "y"})
    private static class Room {
        final int x, y;
        final EnumSet<Direction> directions = EnumSet.noneOf(Direction.class);
    }
    
    @RequiredArgsConstructor
    enum Direction {
        N(0, -1),
        E(-1, 0),
        S(0, 1),
        W(1, 0);
        
        public final int x, y;
        
        Direction getOpposite() {
            return values()[(ordinal() + 2) % 4];
        }
    }
    
    @Value
    private static class Node {
        Room room;
        int distance;
    }
    
    private Map<Point, Room> rooms = new HashMap<>();

    @Override
    protected Result doParts() {
        String input = blob();
        input = input.substring(1, input.length() - 1);
        
        rooms.put(new Point(0, 0), new Room(0, 0));
        parse(input.toCharArray(), 0, 0, 0);
        
        Queue<Node> toSearch = new ArrayDeque<>();
        Set<Room> seen = new HashSet<>();
        toSearch.add(new Node(rooms.get(new Point(0, 0)), 0));
        seen.add(toSearch.peek().getRoom());
        
        int maxDistance = 0;
        int part2 = 0;
        
        while (!toSearch.isEmpty()) {
            Node head = toSearch.poll();
            if (maxDistance < head.getDistance()) {
                maxDistance = head.getDistance();
            }
            if (head.getDistance() >= 1000) {
                part2++;
            }
            for (Direction dir : head.getRoom().getDirections()) {
                int x = head.getRoom().getX() + dir.x;
                int y = head.getRoom().getY() + dir.y;
                Room next = rooms.get(new Point(x, y));
                if (next != null && next.getDirections().contains(dir.getOpposite()) && seen.add(next)) {
                    toSearch.add(new Node(next, head.getDistance() + 1));
                }
            }
        }

        return new Result(maxDistance, part2);
    }
    
    private int parse(char[] input, int pos, int x, int y) {
        char c;
        final int startX = x;
        final int startY = y;
        while (pos < input.length && (c = input[pos]) != ')') {
            pos++;
            Room room = rooms.get(new Point(x, y));
            Direction dir;
            switch(c) {
            case '(':
                pos = parse(input, pos, x, y);
                continue;
            case '|':
                x = startX;
                y = startY;
                continue;
            default:
                dir = Direction.valueOf("" + c);
            }
            x += dir.x;
            y += dir.y;
            Room next = rooms.computeIfAbsent(new Point(x, y), p -> new Room(p.x, p.y));
            room.getDirections().add(dir);
            next.getDirections().add(dir.getOpposite());
        }
        return pos + 1;
    }
}
