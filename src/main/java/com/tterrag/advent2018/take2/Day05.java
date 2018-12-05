package com.tterrag.advent2018.take2;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.tterrag.advent2018.util.Day;

public class Day05 extends Day {

    public static void main(String[] args) {
        new Day05().run();
    }
    
    private Stream<String> units() {
        return IntStream.range('a', 'z' + 1)
                        .mapToObj(i -> "" + (char) i + (char) (i - 32));
    }
    
    private Stream<String> pairs() {
        return units().flatMap(s -> Stream.of(s, "" + s.charAt(1) + s.charAt(0)));
    }
    
    private String react(String in) {
        String res;
        while ((res = pairs().reduce(in, (s1, s2) -> s1.replace(s2, ""))).length() < in.length()) {
            in = res;
        }
        return res;
    }
    
    private final String polymer = blob(); 
    
    @Override
    protected Object part1() {
        return react(polymer).length();
    }
    
    @Override
    protected Object part2() {
        return units().map(s -> polymer.replace(s.substring(0, 1), "").replace(s.substring(1), ""))
                      .map(this::react)
                      .mapToInt(String::length)
                      .min()
                      .getAsInt();
    }
}
