package com.tterrag.advent2018.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import lombok.Value;

public abstract class Day implements Runnable {

    @Value
    @RequiredArgsConstructor
    protected static class Result {

        String p1, p2;

        public Result(Object p1, Object p2) {
            this(Objects.toString(p1), Objects.toString(p2));
        }
        
        public Result(long p1, long p2) {
            this(Long.toString(p1), Long.toString(p2));
        }
        
        public Result(double p1, double p2) {
            this(Double.toString(p1), Double.toString(p2));
        }
    }

    private final List<String> lines = new ArrayList<>();

    @Override
    public final void run() {
        lines(); // Cache file IO so it doesn't affect runtime
        long before = System.nanoTime();
        Result res = doParts();
        long after = System.nanoTime();
        System.out.printf("Part 1: %s\nPart 2: %s\n", res.getP1(), res.getP2());
        System.out.printf("Completed in %.4fs\n\n", (after - before) / 1_000_000_000f);
    }

    protected Object part1() {
        throw new UnsupportedOperationException();
    }

    protected Object part2() {
        throw new UnsupportedOperationException();
    }

    protected Result doParts() {
        return new Result(part1(), part2());
    }
    
    protected String getResourceName() {
        String filename = getClass().getSimpleName().toLowerCase(Locale.ROOT);
        return "/" + filename + ".txt";
    }
    
    protected Stream<String> fileData() {
        String path = getResourceName();
        InputStream in = getClass().getResourceAsStream(path);
        if (in == null) {
            throw new IllegalStateException("Could not find " + path);
        }
        try (BufferedReader r = new BufferedReader(new InputStreamReader(in))) {
            return r.lines();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<String> linesList() {
        if (lines.isEmpty()) {
            lines.addAll(fileData().collect(Collectors.toList()));
        }
        return lines;
    }

    protected Stream<String> lines() {
        return linesList().stream();
    }
    
    protected String[] linesArray() {
        return linesList().toArray(String[]::new);
    }

    protected <T> Stream<T> parse(Function<String, T> parser) {
        return lines().map(parser);
    }

    protected <T> List<T> parseList(Function<String, T> parser) {
        return parse(parser).collect(Collectors.toList());
    }

    protected <E, T extends Collection<E>> Stream<E> parseFlat(Function<String, T> parser) {
        return lines().flatMap(s -> parser.apply(s).stream());
    }

    protected <E, T extends Collection<E>> List<E> parseFlatList(Function<String, T> parser) {
        return parseFlat(parser).collect(Collectors.toList());
    }

    protected String blob() {
        return blob("\n");
    }

    protected String blob(String delim) {
        return lines().collect(Collectors.joining(delim));
    }
}
