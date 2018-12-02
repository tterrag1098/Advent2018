package com.tterrag.advent2018.days;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.tterrag.advent2018.util.Day;

public class Day02 extends Day {

    public static void main(String[] args) {
        new Day02().run();
    }
    
    private boolean hasLetterCount(String str, int count) {
        Map<Integer, Integer> occurances = str.chars().boxed().collect(Collectors.toMap(Function.identity(), c -> 1, (c1, c2) -> c1 + c2));
        return occurances.values().contains(count);
    }

    @Override
    protected Result doParts() {
        long part1 = lines().filter(s -> hasLetterCount(s, 2)).count() * lines().filter(s -> hasLetterCount(s, 3)).count();
        
        List<String> lines = linesList();
        String match1 = null, match2 = null;
        int closest = Integer.MAX_VALUE;
        for (String s : lines) {
            for (String s2 : lines) {
                if (s.equals(s2)) continue;
                int missing = 0;
                List<Integer> charList = s.chars().boxed().collect(Collectors.toList());
                char[] chars = s2.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    if (charList.get(i) != (int) chars[i]) {
                        missing++;
                    }
                }
                if (missing < closest) {
                    closest = missing;
                    match1 = s;
                    match2 = s2;
                }
            }
        }
        
        final String m2 = match2;
        
        return new Result(part1, match1.chars().filter(c -> m2.contains("" + ((char) c))).mapToObj(c -> "" + (char) c).collect(Collectors.joining()));
    }
}
