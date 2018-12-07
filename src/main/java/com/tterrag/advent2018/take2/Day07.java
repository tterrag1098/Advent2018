package com.tterrag.advent2018.take2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.tterrag.advent2018.util.Day;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

public class Day07 extends Day {

    public static void main(String[] args) {
        new Day07().run();
    }
    
    @Value
    @EqualsAndHashCode(of = "id")
    @ToString(of = "id")
    private static class Node implements Comparable<Node> {
        List<Node> next = new ArrayList<>();
        List<Node> prev = new ArrayList<>();
        
        final String id;

        @Override
        public int compareTo(Node o) {
            return id.compareTo(o.getId());
        }
    }
    
    private Map<String, Node> nodes = new HashMap<>(); {
        for (String s : linesList()) {
            Matcher m = PATTERN.matcher(s);
            m.matches();
            Node prev = nodes.computeIfAbsent(m.group(1), Node::new);
            Node next = nodes.computeIfAbsent(m.group(2), Node::new);
            prev.getNext().add(next);
            next.getPrev().add(prev);
        }
    }
    
    private List<Node> roots = nodes.values().stream().filter(n -> n.getPrev().isEmpty()).collect(Collectors.toList());
    
    private static final Pattern PATTERN = Pattern.compile("Step (\\w+) must be finished before step (\\w+) can begin.");
    
    @Override
    protected Object part1() {
        Queue<Node> toSearch = new PriorityQueue<>(roots);
        Set<Node> path = new LinkedHashSet<>();
        while (!toSearch.isEmpty()) {
            Node n = toSearch.poll();
            path.add(n);
            for (Node no : n.getNext()) {
                if (path.containsAll(no.getPrev())) {
                    toSearch.add(no);
                }
            }
        }
        return path.stream().map(Node::getId).collect(Collectors.joining());
    }
    
    @Override
    protected Object part2() {
        Map<Node, Integer> inProgress = new HashMap<>();
        Queue<Node> toSearch = new PriorityQueue<>(roots);
        Set<Node> path = new LinkedHashSet<>();
        int timePassed = 0;
        while (true) {
            
            if (!toSearch.isEmpty()) {
                while (inProgress.size() < 5 && !toSearch.isEmpty()) {
                    Node next = toSearch.poll();
                    inProgress.put(next, next.getId().charAt(0) - 4);
                }
            } else if (inProgress.isEmpty()) {
                break;
            }
            
            Entry<Node, Integer> nextUp = Collections.min(inProgress.entrySet(), Entry.comparingByValue());
            inProgress.remove(nextUp.getKey());
            inProgress.entrySet().forEach(e -> e.setValue(e.getValue() - nextUp.getValue()));
            
            path.add(nextUp.getKey());
            nextUp.getKey().getNext()
                           .stream()
                           .filter(no -> path.containsAll(no.getPrev()))
                           .forEach(no -> toSearch.add(no));
            
            timePassed += nextUp.getValue();
        }
        return timePassed;
    }
}
