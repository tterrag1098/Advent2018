package com.tterrag.advent2018.days;

import java.util.ArrayList;
import java.util.List;

import com.tterrag.advent2018.util.Day;

public class Day14 extends Day {

    public static void main(String[] args) {
        new Day14().run();
    }
    
    private final String input = "047801";

    @Override
    protected Result doParts() {
        
        List<Integer> recipes = new ArrayList<>();
        recipes.add(3);
        recipes.add(7);
        
        int max = Integer.parseInt(input) + 10;
        
        int elf1 = 0, elf2 = 1;
        while (recipes.size() <= max) {
            int recipe1 = recipes.get(elf1);
            int recipe2 = recipes.get(elf2);
            int sum = recipe1 + recipe2;
            char[] newRecipeChars = Integer.toString(sum).toCharArray();
            for (char c : newRecipeChars) {
                recipes.add(Character.digit(c, 10));
            }
            elf1 = (elf1 + 1 + recipe1) % recipes.size();
            elf2 = (elf2 + 1 + recipe2) % recipes.size();
        }
        
        StringBuilder part1 = new StringBuilder();
        for (int i = max - 10; i < max; i++) {
            part1.append(recipes.get(i));
        }
        
        recipes.clear();
        recipes.add(3);
        recipes.add(7);
        
        int[] pattern = input.chars().map(i -> Character.digit((char) i, 10)).toArray();
        
        elf1 = 0;
        elf2 = 1;
        
        while (true) {
            int recipe1 = recipes.get(elf1);
            int recipe2 = recipes.get(elf2);
            int sum = recipe1 + recipe2;
            char[] newRecipeChars = Integer.toString(sum).toCharArray();
            for (char c : newRecipeChars) {
                recipes.add(Character.digit(c, 10));
            }
            elf1 = (elf1 + 1 + recipe1) % recipes.size();
            elf2 = (elf2 + 1 + recipe2) % recipes.size();
            boolean matches = false;
            int off = 0;
            outer:
            for (; !matches && off < newRecipeChars.length; off++) {
                for (int i = 0; i < pattern.length; i++) {
                    int idx = recipes.size() - 1 - i;
                    if (idx < 0) {
                        continue outer;
                    }
                    int recipe = recipes.get(recipes.size() - 1 - i - off);
                    if (recipe != pattern[pattern.length - i - 1]) {
                        continue outer;
                    }
                }
                matches = true;
            }
            if (matches) {
                return new Result(part1, recipes.size() - input.length() + 1 - off);
            }
        }
    }
}
