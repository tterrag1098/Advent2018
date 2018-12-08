package com.tterrag.advent2018.take2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import com.tterrag.advent2018.util.Day;

import lombok.Value;

public class Day08 extends Day {

    public static void main(String[] args) {
        new Day08().run();
    }
    
    private final List<Node> allNodes = new ArrayList<>();
    
    @Value
    private class Node {
        Node parent;
        List<Node> children = new ArrayList<>();
        
        List<Integer> metadata = new ArrayList<>();
        
        Node(Node parent) {
            this.parent = parent;
            if (this.parent != null) {
                this.parent.getChildren().add(this);
            }
            allNodes.add(this);
        }
    }
    
    private final Queue<Integer> input = new ArrayDeque<>(lines().flatMap(s -> Arrays.stream(s.split(" ")))
                                                                 .map(Integer::parseInt)
                                                                 .collect(Collectors.toList()));
    private final Node root = parseTree(new Node(null), input);
    
    private Node parseTree(Node current, Queue<Integer> input) {
        if (input.isEmpty()) {
            return current;
        }
        int children = input.poll();
        int metadata = input.poll();
        for (int i = 0; i < children; i++) {
            Node n = new Node(current);
            parseTree(n, input);
        }
        for (int i = 0; i < metadata; i++) {
            current.getMetadata().add(input.poll());
        }
        return current;
    }
    
    @Override
    protected Object part1() {
        return allNodes.stream().flatMap(n -> n.getMetadata().stream()).mapToInt(Integer::intValue).sum();
    }
    
    @Override
    protected Object part2() {
        return getValue(root);
    }

    private int getValue(Node n) {
        if (n.getChildren().isEmpty()) {
            return n.getMetadata().stream().mapToInt(Integer::intValue).sum();
        }
        int sum = 0;
        for (int i : n.getMetadata()) {
            if (i <= n.getChildren().size()) {
                sum += getValue(n.getChildren().get(i - 1));
            }
        }
        return sum;
    }
}
