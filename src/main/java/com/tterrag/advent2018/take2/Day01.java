package com.tterrag.advent2018.take2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tterrag.advent2018.util.Day;

public class Day01 extends Day {

    public static void main(String[] args) {
        new Day01().run();
    }

    @Override
    protected Result doParts() {
        int part1 = lines().mapToInt(Integer::parseInt).sum();
            
        Set<Integer> seen = new HashSet<>();
        List<Integer> values = parseList(Integer::parseInt);
        int part2 = 0, idx = 0;
        while (seen.add(part2)) {
            part2 += values.get(idx++);
            if (idx >= values.size()) {
                idx = 0;
            }
        }
        return new Result(part1, part2);
    }
}
