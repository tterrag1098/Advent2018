package com.tterrag.advent2018.days;

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
    
    private static char globalId = 1;
    
    @Value
    private static class Node {
        int id = globalId++;
        Node parent;
        List<Node> children = new ArrayList<>();
        
        List<Integer> metadata = new ArrayList<>();
        
        Node(Node parent) {
            this.parent = parent;
            if (this.parent != null) {
                this.parent.getChildren().add(this);
            }
        }
        
        @Override
        public String toString() {
            return (parent == null ? "" : (parent.getId() + " -> ")) + "[" + id + "](" + metadata + ") -> " + children.stream().map(Node::getId).collect(Collectors.toList()); 
        }
    }
    
    private List<Node> allNodes = new ArrayList<>();

    @Override
    protected Result doParts() {
        Queue<Integer> input = new ArrayDeque<>();
        input.addAll(lines().flatMap(s -> Arrays.stream(s.split(" "))).map(Integer::parseInt).collect(Collectors.toList()));
        
        Node root = new Node(null);
        allNodes.add(root);
        parseTree(root, input);
        
        return new Result(allNodes.stream().flatMap(n -> n.getMetadata().stream()).mapToInt(Integer::intValue).sum(), getValue(root));
    }

    private void parseTree(Node current, Queue<Integer> input) {
        if (input.isEmpty()) {
            return;
        }
        int children = input.poll();
        int metadata = input.poll();
        for (int i = 0; i < children; i++) {
            Node n = new Node(current);
            allNodes.add(n);
            parseTree(n, input);
        }
        for (int i = 0; i < metadata; i++) {
            current.getMetadata().add(input.poll());
        }
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
