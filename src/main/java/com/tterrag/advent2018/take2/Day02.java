package com.tterrag.advent2018.take2;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.tterrag.advent2018.util.Day;

public class Day02 extends Day {

    public static void main(String[] args) {
        new Day02().run();
    }
    
    private boolean hasLetterCount(String str, long count) {
        return str.chars().boxed().collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).values().contains(count);
    }

    @Override
    protected Result doParts() {
        long part1 = lines().filter(s -> hasLetterCount(s, 2)).count() * lines().filter(s -> hasLetterCount(s, 3)).count();
        
        List<String> lines = linesList();
        String part2 = null;
        int closest = Integer.MAX_VALUE;
        for (String s : lines) {
            for (String s2 : lines) {
                if (s.equals(s2)) continue;
                int missing = 0;
                char[] chars1 = s.toCharArray();
                char[] chars2 = s2.toCharArray();
                for (int i = 0; i < chars1.length; i++) {
                    if (chars1[i] != chars2[i]) {
                        missing++;
                    }
                }
                if (missing < closest) {
                    closest = missing;
                    part2 = s.chars().filter(c -> s2.contains("" + ((char) c))).mapToObj(c -> "" + (char) c).collect(Collectors.joining());
                }
            }
        }

        return new Result(part1, part2);
    }
}
