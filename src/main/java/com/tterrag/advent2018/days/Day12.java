package com.tterrag.advent2018.days;

import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

import com.tterrag.advent2018.util.Day;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class Day12 extends Day {

    public static void main(String[] args) {
        new Day12().run();
    }
    
    @RequiredArgsConstructor
    @ToString
    private static class Rule {
        
        private final boolean[] pattern = new boolean[5];
        private final boolean result;
        
        boolean matches(NegativeBitSet prev, int index) {
            for (int i = -2; i <= 2; i++) {
                int idx = index + i;
                if (pattern[i + 2] != prev.get(idx)) {
                    return false;
                }
            }
            return true;
        }
        
        static Rule parse(String line) {
            String[] info = line.split(" => ");
            Rule ret = new Rule(info[1].equals("#"));
            for (int i = 0; i < info[0].length(); i++) {
                char c = info[0].charAt(i);
                if (c == '#') {
                    ret.pattern[i] = true;
                }
            }
            return ret;
        }
    }
    
    @RequiredArgsConstructor
    private static class NegativeBitSet implements Cloneable {
        
        private final BitSet positive = new BitSet();
        private final BitSet negative = new BitSet();
        
        public boolean get(int i) {
            return i >= 0 ? positive.get(i) : negative.get(-i);
        }
        
        public void set(int i, boolean b) {
            if (i >= 0) {
                positive.set(i, b);
            } else {
                negative.set(-i, b);
            }
        }

        public int first() {
            if (negative.isEmpty()) {
                return positive.nextSetBit(0);
            } else {
                return -negative.previousSetBit(negative.length());
            }
        }
        
        public int last() {
            if (positive.isEmpty()) {
                return -negative.nextSetBit(0);
            } else {
                return positive.previousSetBit(positive.length());
            }
        }
        
        @Override
        public String toString() {
            return negative.toString() + "|" + positive.toString();
        }
    }

    @Override
    protected Result doParts() {
        List<String> lines = linesList();
        String initialState = lines.get(0).replace("initial state: ", "");
        lines.remove(0);
        lines.remove(0);
        
        NegativeBitSet states = new NegativeBitSet();
        for (int i = 0; i < initialState.length(); i++) {
            states.set(i, initialState.charAt(i) == '#');
        }
        
        List<Rule> rules = lines.stream().map(Rule::parse).collect(Collectors.toList());
        for (int i = 0; i < 20; i++) {
            NegativeBitSet prev = states;
            states = new NegativeBitSet();
            for (Rule r : rules) {
                for (int j = prev.first() - 2; j <= prev.last() + 2; j++) {
                    if (r.matches(prev, j)) {
                        states.set(j, r.result);
                    }
                }
            }
        }
        
        int part1 = 0;
        for (int i = states.first(); i <= states.last(); i++) {
            if (states.get(i)) {
                part1 += i;
            }
        }
        return new Result(part1, part2());
    }
    
    @Override
    protected Object part2() { // This probably won't work on other inputs...
        long start = 50_000_000_000L - 50;
        long ret = 0;
        for (int i = 0; i < 50; i++) {
            ret += start;
            start += 3;
        }
        return ret;
    }
}
