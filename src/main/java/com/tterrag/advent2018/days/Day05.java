package com.tterrag.advent2018.days;

import com.tterrag.advent2018.util.Day;

public class Day05 extends Day {

    public static void main(String[] args) {
        new Day05().run();
    }
    
    private String remove(char c, String in) {
        in = in.replace("" + c + Character.toUpperCase(c), "");
        in = in.replace("" + Character.toUpperCase(c) + c, "");
        return in;
    }
    
    private String react(String in) {
        boolean finished = false;
        while (!finished) {
            finished = true;
            for (char c = 'a'; c <= 'z'; c++) {
                String res = remove(c, in);
                if (res.length() < in.length()) {
                    finished = false;
                }
                in = res;
            }
        }
        return in;
    }

    @Override
    protected Result doParts() {        
        String input = blob();
        
        int shortest = Integer.MAX_VALUE;
        for (char c = 'a'; c <= 'z'; c++) {
            String withoutChar = input.replace("" + c, "");
            withoutChar = withoutChar.replace("" + Character.toUpperCase(c), "");
            String reacted = react(withoutChar);
            if (reacted.length() < shortest) {
                shortest = reacted.length();
            }
        }

        return new Result(react(input).length(), shortest);
    }
}
