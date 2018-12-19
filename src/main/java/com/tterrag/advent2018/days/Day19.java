package com.tterrag.advent2018.days;

import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;

import com.tterrag.advent2018.util.Day;

import lombok.RequiredArgsConstructor;
import lombok.Value;

public class Day19 extends Day {

    public static void main(String[] args) {
        new Day19().run();
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
        
        public final boolean registerA, registerB;
        public final IntBinaryOperator func;
        
        public void apply(int[] registers, int a, int b, int c) {
            if (registerA) {
                a = registers[a];
            }
            if (registerB) {
                b = registers[b];
            }
            registers[c] = func.applyAsInt(a, b);
        }
    }
    
    @Value
    private static class Instruction {
        Opcode op;
        int a, b, c;
        
        void apply(int[] registers) {
            op.apply(registers, a, b, c);
        }
    }

    @Override
    protected Result doParts() {
        List<String> lines = linesList();
        
        String ipHeader = lines.remove(0);
        int instructionPointer = Integer.parseInt(ipHeader.substring(4));
        int instruction = 0;
        
        List<Instruction> instrs = lines.stream().map(s -> s.split(" "))
                .map(arr -> new Instruction(Opcode.valueOf(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3])))
                .collect(Collectors.toList());
        
        int[] registers = new int[6];
        instruction = registers[instructionPointer];
        
        while (instruction >= 0 && instruction < instrs.size()) {
            registers[instructionPointer] = instruction;
            Instruction current = instrs.get(registers[instructionPointer]);
            current.apply(registers);
            instruction = registers[instructionPointer];
            instruction++;
        }
        
        return new Result(registers[0], optimized());
    }
    
    private int optimized() {
        int[] r = { 1, 0, 0, 0, 0, 0 };
        /*17: */r[3]+= 2;
        /*18: */r[3]*= r[3];
        /*19: */r[3]*= 19;
        /*20: */r[3]*= 11;
        /*21: */r[4]+= 7;
        /*22: */r[4]*= 22;
        /*23: */r[4]+= 6;
        /*24: */r[3]+= r[4];
        /*27: */r[4] = 27;
        /*28: */r[4]*= 28;
        /*29: */r[4]+= 29;
        /*30: */r[4]*= 30;
        /*31: */r[4]*= 14;
        /*32: */r[4]*= 32;
        /*33: */r[3]+= r[4];
        /*34: */r[0] = 0;
        /*35: *///goto 1;
        /*1:  */r[1] = 1;
        do {
            /*2:  */r[5] = 1;
            if (r[3] % r[1] == 0) {
                r[0] += r[1];
            }
//            do {
//                System.out.println(Arrays.toString(r));
//                /*3:  */r[4] = r[1] * r[5];
//                if (r[4] == r[3]) {
//                    /*7:  */r[0]+= r[1];
//                }
//                /*8:  */r[5]++;
//            } while(r[5] <= r[3]);
            /*12: */r[1]++;
        } while(r[1] <= r[3]);
        /*16: */return r[0];
    }
    
    /*
// ip=2;
0:  goto 17;
1:  r[1] = 1;
2:  r[5] = 1;
3:  r[4] = r[1] * r[5];
4:  r[4] = r[4] == r[3] ? 1 : 0;
5:  r[2]+= r[4];
6:  r[2]++
7:  r[0]+= r[1];
8:  r[5]++
9:  r[4] = r[5] > r[3] ? 1 : 0;
10: r[2]+= r[4];
11: goto 3;
12: r[1]++
13: r[4] = r[1] > r[3] ? 1 : 0;
14: r[2]+= r[4];
15: r[2] = 1;
16: r[2]*= r[2];
17: r[3]+= 2;
18: r[3]*= r[3];
19: r[3]*= r[2];
20: r[3]*= 11;
21: r[4]+= 7;
22: r[4]*= r[2];
23: r[4]+= 6;
24: r[3]+= r[4];
25: goto 25 + r[0] + 1;
26: r[3] = 0;
27: r[4] = r[2];
28: r[4]*= r[2];
29: r[4]+= r[2];
30: r[4]*= r[2];
31: r[4]*= 14;
32: r[4]*= r[2];
33: r[3]+= r[4];
34: r[0] = 0;
35: goto 1;
*/
}
