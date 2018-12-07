package com.tterrag.advent2018.days;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.tterrag.advent2018.util.Day;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class Day07 extends Day {

    public static void main(String[] args) {
        new Day07().run();
    }
    
    
    @Getter
    @RequiredArgsConstructor
    private static class Node implements Comparable<Node> {
        List<Node> next = new ArrayList<>();
        List<Node> prev = new ArrayList<>();
        
        final String id;
        
        @Override
        public String toString() {
            return prev.stream().map(Node::getId).collect(Collectors.joining(",")) + " -> " + id + " -> " + next.stream().map(Node::getId).collect(Collectors.joining(","));
        }
        
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Node && ((Node)obj).getId().equals(id);
        }
        
        @Override
        public int hashCode() {
            return id.hashCode();
        }
        
        @Override
        public int compareTo(Node o) {
            return id.compareTo(o.getId());
        }
    }
    
    private Map<String, Node> nodes = new HashMap<>();
    
    private static final Pattern PATTERN = Pattern.compile("Step (\\w+) must be finished before step (\\w+) can begin.");

    @Override
    protected Result doParts() {
        for (String s : linesList()) {
            Matcher m = PATTERN.matcher(s);
            m.matches();
            Node prev = nodes.computeIfAbsent(m.group(1), Node::new);
            Node next = nodes.computeIfAbsent(m.group(2), Node::new);
            prev.getNext().add(next);
            next.getPrev().add(prev);
        }
        
        Queue<Node> toSearch = nodes.values().stream().filter(n -> n.getPrev().isEmpty()).collect(Collectors.toCollection(PriorityQueue::new));
        
        nodes.values().forEach(System.out::println);
        
        Set<Node> part1 = new LinkedHashSet<>();
        while (!toSearch.isEmpty()) {
            Node n = toSearch.poll();
            part1.add(n);
            for (Node no : n.getNext()) {
                if (part1.containsAll(no.getPrev())) {
                    toSearch.add(no);
                }
            }
        }
        
        Map<Node, Integer> inProgress = new HashMap<>();
        toSearch = nodes.values().stream().filter(n -> n.getPrev().isEmpty()).collect(Collectors.toCollection(PriorityQueue::new));
        Set<Node> path = new LinkedHashSet<>();
        int part2 = 0;
        while (true) {
            if (!toSearch.isEmpty()) {
                while (inProgress.size() < 5 && !toSearch.isEmpty()) {
                    Node next = toSearch.poll();
                    inProgress.put(next, next.getId().charAt(0) - 4);
                }
            } else if (inProgress.isEmpty()) {
                break;
            }
            for (Node n : new HashSet<>(inProgress.keySet())) {
                Integer res = inProgress.merge(n, 1, (i1, i2) -> (i1 - i2) == 0 ? null : i1 - i2);
                if (res == null) {
                    path.add(n);
                    for (Node no : n.getNext()) {
                        if (path.containsAll(no.getPrev())) {
                            toSearch.add(no);
                        }
                    }
                }
            }
            part2++;
        }
        
        System.out.println(path.stream().map(Node::getId).collect(Collectors.joining()));

        return new Result(part1.stream().map(Node::getId).collect(Collectors.joining()), part2);
    }
}
