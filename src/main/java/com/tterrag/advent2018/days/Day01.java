package com.tterrag.advent2018.days;

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
        int part1 = lines().reduce(0, (i, s1) -> Integer.parseInt(s1) + i, (i1, i2) -> i1 + i2);
        
        Set<Integer> seen = new HashSet<>();
        int tot = 0;
        seen.add(tot);
        List<Integer> values = parseList(Integer::parseInt);
        int idx = 0;
        while (true) {
            tot += values.get(idx++);
            if (idx >= values.size()) {
                idx = 0;
            }
            if (seen.contains(tot)) {
                return new Result(part1, tot);
            }
            seen.add(tot);
        }
    }
}
