package com.tterrag.advent2018.days;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.tterrag.advent2018.util.Day;

import lombok.RequiredArgsConstructor;

public class Day18 extends Day {

    public static void main(String[] args) {
        new Day18().run();
    }
    
    @RequiredArgsConstructor
    enum State {
        OPEN('.'),
        TREES('|'),
        LUMBERYARD('#');
        
        private final char key;
        
        static State byKey(char c) {
            for (State state : values()) {
                if (state.key == c) {
                    return state;
                }
            }
            throw new IllegalArgumentException();
        }
    }
    
    private long getResourceValue(State[][] area) {
        Map<State, Long> counts = Arrays.stream(area).flatMap(Arrays::stream).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        long score = counts.get(State.TREES) * counts.get(State.LUMBERYARD);
        return score;
    }

    @Override
    protected Result doParts() {
        State[][] area = lines().map(s -> s.chars().mapToObj(c -> State.byKey((char) c)).toArray(State[]::new)).toArray(State[][]::new);
        
        long part1 = 0;        
        List<Long> loop = new ArrayList<>();

        for (int i = 0;; i++) {
            if (i == 10) {
                part1 = getResourceValue(area);
            }
            if (i >= 10_000) {
                long score = getResourceValue(area);
                if (loop.isEmpty() || loop.get(0) != score) {
                    loop.add(score);
                } else {
                    break;
                }
            }
            
            State[][] newState = new State[area.length][];
            for (int y = 0; y < area.length; y++) {
                newState[y] = area[y].clone();
            }
            for (int y = 0; y < area.length; y++) {
                for (int x = 0; x < area[y].length; x++) {
                    State current = area[y][x];
                    Map<State, Integer> neighbors = new HashMap<>();
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            if (dy == 0 && dx == 0) {
                                continue;
                            }
                            int ny = y + dy;
                            int nx = x + dx;
                            if (ny >= 0 && ny < area.length && nx >= 0 && nx < area[ny].length) {
                                neighbors.merge(area[ny][nx], 1, Integer::sum);
                            }
                        }
                    }
                    if (current == State.OPEN && neighbors.getOrDefault(State.TREES, 0) >= 3) {
                        newState[y][x] = State.TREES;
                    } else if (current == State.TREES && neighbors.getOrDefault(State.LUMBERYARD, 0) >= 3) {
                        newState[y][x] = State.LUMBERYARD;
                    } else if (current == State.LUMBERYARD) {
                        if (!neighbors.containsKey(State.LUMBERYARD) || !neighbors.containsKey(State.TREES)) {
                            newState[y][x] = State.OPEN;
                        }
                    }
                }
            }
            area = newState;
        }
        
        int loopSize = loop.size();
        long part2 = loop.get((1_000_000_000 - 10_000) % loopSize);

        return new Result(part1, part2);
    }
}
