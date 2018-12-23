package com.tterrag.advent2018.days;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.tterrag.advent2018.util.Day;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

public class Day22 extends Day {

    public static void main(String[] args) {
        new Day22().run();
    }
    
    @Value
    private static class Point {
        int x, y;
        
        Point move(Direction dir) {
            return new Point(x + dir.x, y + dir.y);
        }
    }
    
    @RequiredArgsConstructor
    enum Direction {
        N(0, -1),
        E(-1, 0),
        S(0, 1),
        W(1, 0);
        
        public final int x, y;

        public static Direction[] valid(Point pos) {
            if (pos.getX() > 0 && pos.getY() > 0) {
                return values();
            } else if (pos.getX() > 0) {
                return new Direction[] { E, S, W };
            } else if (pos.getY() > 0) {
                return new Direction[] { N, S, W };
            } else {
                return new Direction[] { S, W };
            }
        }
    }
    
    enum Tool {
        CLIMBING_GEAR,
        TORCH,
        NONE,
    }
    
    enum Type {
        ROCKY(Tool.CLIMBING_GEAR, Tool.TORCH),
        WET(Tool.CLIMBING_GEAR, Tool.NONE),
        NARROW(Tool.TORCH, Tool.NONE);
        
        private final EnumSet<Tool> validTools;
        
        private Type(Tool first, Tool... rest) {
            validTools = EnumSet.of(first, rest);
        }
        
        boolean canUse(Tool tool) {
            return validTools.contains(tool);
        }
        
        static Type fromErosion(long erosion) {
            return values()[(int) (erosion % 3L)];
        }
    }
    
    @Value
    @ToString(of = {"tool", "pos", "time"})
    private static class Node {
        Tool tool;
        int time;
        Point pos;
        List<Node> path = new ArrayList<>();
        
        Node move(Direction dir) {
            Node ret = new Node(tool, time + 1, pos.move(dir));
            ret.path.addAll(path);
            ret.path.add(this);
            return ret;
        }
        
        Node withTool(Tool tool) {
            Node ret = new Node(tool, time + 7, pos);
            ret.path.addAll(path);
            ret.path.add(this);
            return ret;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Node other = (Node) obj;
            if (pos == null) {
                if (other.pos != null)
                    return false;
            } else if (!pos.equals(other.pos))
                return false;
            if (tool != other.tool)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((pos == null) ? 0 : pos.hashCode());
            result = prime * result + ((tool == null) ? 0 : tool.hashCode());
            return result;
        }
    }
    
    private final int depth = 7740;
    private final Point target = new Point(12, 763);
    
    private long getErosion(long index) {
        return (index + depth) % 20183;
    }
    
    private long getErosion(Map<Point, Long> cache, int x, int y) {
        Point p = new Point(x, y);
        if (x == 0 && y == 0) {
            cache.put(p, getErosion(0L));
        } else if (x == target.getX() && y == target.getY()) {
            cache.put(p, getErosion(0L));
        } else if (y == 0) {
            cache.put(p, getErosion((x * 16807L) % 20183L));
        } else if (x == 0) {
            cache.put(p, getErosion((y * 48271L) % 20183L));
        } else {
            Long cached = cache.get(p);
            if (cached == null) {
                cache.put(p, getErosion((getErosion(cache, p.x - 1, p.y) * getErosion(cache, p.x, p.y - 1)) % 20183L));
            }
        }
        return cache.get(p);
    }
    
    private Type getType(Map<Point, Long> cache, int x, int y) {
        return Type.fromErosion(getErosion(cache, x, y));
    }
    
    private int index(Node n) {
        return (n.getPos().getX() << 17) | (n.getPos().getY() << 2) | n.getTool().ordinal();
    }

    @Override
    protected Result doParts() {
        Map<Point, Long> erosionCache = new HashMap<>();
        Map<Point, Type> typeCache = new HashMap<>();

        int risk = 0;
        
        for (int y = 0; y < target.getY() + 1; y++) {
            for (int x = 0; x < target.getX() + 1; x++) {
                Type type = getType(erosionCache, x, y);
                risk += type.ordinal();
                typeCache.put(new Point(x, y), type);
            }
        }
        
        PriorityQueue<Node> search = new PriorityQueue<>(Comparator.comparingInt(Node::getTime));
        search.add(new Node(Tool.TORCH, 0, new Point(0, 0)));
        
        BitSet seen = new BitSet();
        int[] timings = new int[200_000_000];
        
        Node endNode = new Node(Tool.TORCH, 0, target);
        
        while (!search.isEmpty()) {
            Node head = search.poll();
            if (head.equals(endNode)) {
                return new Result(risk, head.getTime());
            }
            Type type = typeCache.get(head.getPos());
            Set<Node> validNext = new LinkedHashSet<>();
            Direction[] valid = Direction.valid(head.getPos());
            for (Direction d : valid) {
                Node next = head.move(d);
                Type t = typeCache.computeIfAbsent(next.getPos(), p -> getType(erosionCache, p.getX(), p.getY()));
                if (t.canUse(next.getTool())) {
                    validNext.add(next);
                }
            }
            for (Tool tool : Tool.values()) {
                if (tool != head.getTool() && type.canUse(tool)) {
                    validNext.add(head.withTool(tool));
                }
            }
            validNext.removeIf(n -> {
                int time = n.getTime();
                int oldtime = timings[index(n)];
                if (oldtime == 0 || oldtime > time) {
                    timings[index(n)] = time;
                    return false;
                }
                return true;
            });
            validNext.forEach(n -> seen.set(index(n))); 
            search.addAll(validNext);
        }
        
        return null;
    }
}
