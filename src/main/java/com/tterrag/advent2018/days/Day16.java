package com.tterrag.advent2018.days;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.IntBinaryOperator;

import com.tterrag.advent2018.util.Day;

import lombok.RequiredArgsConstructor;

public class Day16 extends Day {

    public static void main(String[] args) {
        new Day16().run();
    }
    
    @RequiredArgsConstructor
    enum Opcode {
        addr(true, true, Integer::sum),
        addi(true, false, Integer::sum),
        mulr(true, true, (a, b) -> a * b),
        muli(true, false, (a, b) -> a * b),
        banr(true, true, (a, b) -> a & b),
        bani(true, false, (a, b) -> a & b),
        borr(true, true, (a, b) -> a | b),
        bori(true, false, (a, b) -> a | b),
        setr(true, false, (a, b) -> a),
        seti(false, false, (a, b) -> a),
        gtir(false, true, (a, b) -> a > b ? 1 : 0),
        gtri(true, false, (a, b) -> a > b ? 1 : 0),
        gtrr(true, true, (a, b) -> a > b ? 1 : 0),
        eqir(false, true, (a, b) -> a == b ? 1 : 0),
        eqri(true, false, (a, b) -> a == b ? 1 : 0),
        eqrr(true, true, (a, b) -> a == b ? 1 : 0),
        ;
        
        private final boolean registerA, registerB;
        private final IntBinaryOperator func;
    }
    
    private int[] parseRegisters(String text) {
        String[] values = text.substring(1, text.length() - 1).split(", ");
        return Arrays.stream(values).mapToInt(Integer::parseInt).toArray();
    }
    
    private boolean valid(Opcode opcode, int[] instr, int[] before, int[] after) {
        if (before.length != after.length || instr.length != 4) {
            return false;
        }
        int a = instr[1], b = instr[2], c = instr[3];
        int[] res = new int[before.length];
        System.arraycopy(before, 0, res, 0, before.length);
        if (opcode.registerA) {
            if (a >= before.length) {
                return false;
            }
            a = before[a];
        }
        if (opcode.registerB) {
            if (b >= before.length) {
                return false;
            }
            b = before[b];
        }
        res[c] = opcode.func.applyAsInt(a, b);
        for (int i = 0; i < before.length; i++) {
            if (res[i] != after[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected Result doParts() {
        List<String> lines = linesList();
        Iterator<String> iter = lines.iterator();
        
        Map<Integer, EnumSet<Opcode>> possibleMap = new HashMap<>();
        int part1 = 0;
        
        while (iter.hasNext()) {
            String beforeLine = iter.next();
            if (!beforeLine.startsWith("Before:")) {
                break;
            }
            String instrLine = iter.next();
            String afterLine = iter.next();
            
            int[] registersBefore = parseRegisters(beforeLine.replace("Before: ", ""));
            int[] registersAfter = parseRegisters(afterLine.replaceAll("After:  ", ""));
            
            int[] instr = Arrays.stream(instrLine.split(" ")).mapToInt(Integer::parseInt).toArray();
            
            int opcode = instr[0];
            EnumSet<Opcode> allPossible = EnumSet.allOf(Opcode.class);
            EnumSet<Opcode> possible = possibleMap.computeIfAbsent(opcode, $ -> EnumSet.allOf(Opcode.class));
            
            for (Opcode op : Opcode.values()) {
                if (!valid(op, instr, registersBefore, registersAfter)) {
                    allPossible.remove(op);
                    possible.remove(op);
                }
            }
            
            if (allPossible.size() >= 3) {
                part1++;
            }
            
            iter.next();
        }
        
        Map<Integer, Opcode> opcodes = new HashMap<>();
        while (!possibleMap.isEmpty()) {
            Entry<Integer, EnumSet<Opcode>> certain = possibleMap.entrySet().stream().filter(e -> e.getValue().size() == 1).findFirst().get();
            Opcode op = certain.getValue().iterator().next();
            opcodes.put(certain.getKey(), op);
            possibleMap.remove(certain.getKey());
            possibleMap.values().forEach(s -> s.remove(op));
        }
        
        int[] registers = new int[4];
        while (iter.hasNext()) {
            String line = iter.next();
            if (line.isEmpty()) {
                continue;
            }
            int[] instr = Arrays.stream(line.split(" ")).mapToInt(Integer::parseInt).toArray();
            Opcode op = opcodes.get(instr[0]);
            int a = instr[1], b = instr[2], c = instr[3];
            if (op.registerA) {
                a = registers[a];
            }
            if (op.registerB) {
                b = registers[b];
            }
            registers[c] = op.func.applyAsInt(a, b);
        }

        return new Result(part1, registers[0]);
    }
}
