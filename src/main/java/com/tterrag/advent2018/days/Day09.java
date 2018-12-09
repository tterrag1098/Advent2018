package com.tterrag.advent2018.days;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.tterrag.advent2018.util.Day;

public class Day09 extends Day {

    public static void main(String[] args) {
        new Day09().run();
    }
    
    @Override
    protected List<String> linesList() {
        return Collections.emptyList();
    }
    
    private final int players = 416;
    private final int points = 71975;
    
    private final LinkedList<Integer> marbles = new LinkedList<>();
    private final Map<Integer, Long> scores = new HashMap<>();

    @Override
    protected Result doParts() {
        int nextMarble = 0;
        int turn = 0;
        marbles.add(nextMarble++);
        ListIterator<Integer> iter = marbles.listIterator();
        iter.next();
        long part1 = 0;
        while (nextMarble <= points * 100) {
            if (nextMarble % 23 == 0) {
                for (int i = 0; i < 7; i++) {
                    if (!iter.hasPrevious()) {
                        iter = marbles.listIterator(marbles.size());
                    }
                    iter.previous();
                }
                if (!iter.hasPrevious()) {
                    iter = marbles.listIterator(marbles.size());
                }
                int removed = iter.previous();
                iter.remove();
                iter.next();
                scores.merge(turn, (long) (nextMarble++ + removed), Long::sum);
            } else {
                if (!iter.hasNext()) {
                    iter = marbles.listIterator();
                }
                iter.next();
                iter.add(nextMarble++);
            }
            turn = (turn + 1) % players;
            if (nextMarble == points + 1) {
                part1 = scores.values().stream().mapToLong(Long::longValue).max().getAsLong();
            }
        }

        return new Result(part1, scores.values().stream().mapToLong(Long::longValue).max().getAsLong());
    }
}
