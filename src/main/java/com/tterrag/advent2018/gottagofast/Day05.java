package com.tterrag.advent2018.gottagofast;

import java.util.ArrayDeque;
import java.util.Deque;

import com.tterrag.advent2018.util.Day;

public class Day05 extends Day {

    public static void main(String[] args) {
        new Day05().run();
    }
    
    private char pairOf(char c) {
        return (char) (c > 96 ? c - 32 : c + 32);
    }
    
    private String react(String in) {
        Deque<Character> res = new ArrayDeque<>();
        for (char c : in.toCharArray()) {
            if (!res.isEmpty()) {
                char prev = res.peek();
                if (c == pairOf(prev)) {
                    res.pop();
                } else {
                    res.push(c);
                }
            } else {
                res.push(c);
            }
        }
        return res.stream().reduce(new StringBuilder(), (s, c) -> s.append((char) c), StringBuilder::append).toString();
    }
    
    private final String polymer = blob(); 
    
    @Override
    protected Object part1() {
        return react(polymer).length();
    }
    
    @Override
    protected Object part2() {
        int shortest = Integer.MAX_VALUE;
        for (char c = 'a'; c <= 'z'; c++) {
            String s = polymer.replace("" + c, "").replace("" + pairOf(c), "");
            int len = react(s).length();
            if (len < shortest) {
                shortest = len;
            }
        }
        return shortest;
    }
}
