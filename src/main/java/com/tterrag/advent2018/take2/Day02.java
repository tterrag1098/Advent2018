package com.tterrag.advent2018.take2;

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
        
        String[] lines = linesArray();
        String part2 = null;
        for (int i = 0; i < lines.length; i++) {
            for (int j = i + 1; j < lines.length; j++) {
                String s = lines[i], s2 = lines[j];
                if (s.equals(s2)) continue;
                int missing = 0;
                char[] chars1 = s.toCharArray();
                char[] chars2 = s2.toCharArray();
                for (int c = 0; c < chars1.length && missing <= 1; c++) {
                    if (chars1[c] != chars2[c]) {
                        missing++;
                    }
                }
                if (missing == 1) {
                    int[] intersect = s.chars().filter(c -> s2.indexOf((char) c) != -1).toArray();
                    part2 = new String(intersect, 0, intersect.length);
                    return new Result(part1, part2);
                }
            }
        }
        throw new IllegalStateException("Did not find part 2 answer!");
    }
}
