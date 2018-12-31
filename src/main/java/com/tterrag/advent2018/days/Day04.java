package com.tterrag.advent2018.days;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.tterrag.advent2018.util.Day;

import lombok.Value;

public class Day04 extends Day {

    public static void main(String[] args) {
        new Day04().run();
    }
    
    private enum State {
        AWAKE,
        ASLEEP
    }
    
    @Value
    private static class Range {
        State state;
        int start, end;
    }
    
    @Value
    private static class Shift {
        int guard;
        List<Range> ranges = new ArrayList<>();
    }
    
    @Value
    private static class Time implements Comparable<Time> {
        private static final Pattern TIME = Pattern.compile("\\[(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}).*");
        
        int yr, mo, day, hr, min;
        
        static Time parse(String in) {
            Matcher m = TIME.matcher(in);
            m.matches();
            return new Time(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)));
        }
        
        @Override
        public int compareTo(Time o) {
            return Comparator.comparingInt(Time::getYr)
                             .thenComparingInt(Time::getMo)
                             .thenComparingInt(Time::getDay)
                             .thenComparingInt(Time::getHr)
                             .thenComparingInt(Time::getMin)
                             .compare(this, o);
        }
    }
    
    private static final Pattern PATTERN = Pattern.compile("\\[\\d+-\\d+-\\d+\\s(\\d{2}):(\\d{2})\\]\\s(.+)");
    
    private static final Pattern GUARD = Pattern.compile("Guard\\s#(\\d+)\\sbegins\\sshift");

    @Override
    protected Result doParts() {
        List<String> lines = lines().sorted((s1, s2) -> Time.parse(s1).compareTo(Time.parse(s2))).collect(Collectors.toList());
        
        System.out.println(lines);
        
        List<Shift> shifts = new ArrayList<>();
        
        State prevState = State.AWAKE;
        int prevMinute = 0;
        Shift shift = null;
        
        for (String s : lines) {
            Matcher m = PATTERN.matcher(s);
            if (m.matches()) {
                int hr = Integer.parseInt(m.group(1));
                int min = hr == 0 ? Integer.parseInt(m.group(2)) : 0;
                String event = m.group(3);
                m = GUARD.matcher(event);
                if (m.matches()) {
                    int guard = Integer.parseInt(m.group(1));
                    if (shift != null) {
                        shift.getRanges().add(new Range(prevState, prevMinute, 60));
                        shifts.add(shift);
                    }
                    prevState = State.AWAKE;
                    prevMinute = min;
                    shift = new Shift(guard);
                } else if (shift != null && event.equals("falls asleep")) {
                    shift.getRanges().add(new Range(prevState, prevMinute, min));
                    prevMinute = min;
                    prevState = State.ASLEEP;
                } else if (shift != null && event.equals("wakes up")) {
                    shift.getRanges().add(new Range(prevState, prevMinute, min));
                    prevMinute = min;
                    prevState = State.AWAKE;
                } else {
                    System.out.println("Skipping line: " + s);
                }
            }
        }
        
        Map<Integer, List<Range>> asleepTime = 
            shifts.stream()
                  .collect(Collectors.groupingBy(s -> s.getGuard(), Collectors.flatMapping(s -> s.getRanges().stream().filter(r -> r.getState() == State.ASLEEP), Collectors.toList())));
        
        int asleepMost = asleepTime.entrySet().stream()
                .max(Comparator.comparing(e -> e.getValue().stream().mapToInt(r -> r.getEnd() - r.getStart()).sum()))
                .map(Entry::getKey)
                .get();
        
        int[] sleepCount = new int[60];
        List<Range> ranges = asleepTime.get(asleepMost);
        for (Range r : ranges) {
            for (int i = r.getStart(); i < r.getEnd(); i++) {
                sleepCount[i]++;
            }
        }
        
        int maxIdx = -1, max = -1;
        for (int i = 0; i < sleepCount.length; i++) {
            if (sleepCount[i] > max) {
                maxIdx = i;
                max = sleepCount[i];
            }
        }
        
        int part1 = asleepMost * maxIdx;
        
        Map<Integer, int[]> sleepyMinutes = asleepTime.entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey, e -> {
                    int[] counts = new int[60];
                    for (Range r : e.getValue()) {
                        for (int i = r.getStart(); i < r.getEnd(); i++) {
                            counts[i]++;
                        }
                    }
                    int mxIdx = -1, mx = -1;
                    for (int i = 0; i < counts.length; i++) {
                        if (counts[i] > mx) {
                            mxIdx = i;
                            mx = counts[i];
                        }
                    }
                    return new int[] {mxIdx, mx};
                }));
        
        int part2 = sleepyMinutes.entrySet().stream().max(Entry.comparingByValue(Comparator.comparingInt(arr -> arr[1]))).map(e -> e.getKey() * e.getValue()[0]).get();

        return new Result(part1, part2);
    }
}
