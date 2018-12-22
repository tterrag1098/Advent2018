package com.tterrag.advent2018.days;

import com.tterrag.advent2018.util.Day;

public class Day21 extends Day {

    public static void main(String[] args) {
        new Day21().run();
    }

    @Override
    protected Result doParts() {
        int[] r = new int[6];
        int part1 = 0;
        int lowest = Integer.MAX_VALUE;
        // #ip 1
        /*0:  */r[5] = 123;
        do {
            /*1:  */r[5] = r[5] & 456;
        } /*2:  */while (r[5] != 72); //r[5] = r[5] == 72 ? 1 : 0;
        /*5:  */r[5] = 0;
        do {
            /*6:  */r[3] = r[5] | (1 << 16);
            /*7:  */r[5] = 521363;
            L8: do {
                /*8:  */r[4] = r[3] & 0xFF;
                /*9:  */r[5] += r[4];
                /*10:  */r[5] = r[5] & 0xFFFFFF;
                /*11:  */r[5] = r[5] * 65899;
                /*12:  */r[5] = r[5] & 0xFFFFFF;
                /*13:  */if (256 <= r[3]) { //r[4] = 256 > r[3] ? 1 : 0;
                    /*17:  */r[4] = 0;
                    L18: do {
                        /*18:  */r[2] = r[4] + 1;
                        /*19:  */r[2] *= 256;
                        /*20:  */if (r[2] > r[3]) { //r[2] = r[2] > r[3] ? 1 : 0;
                            /*26:  */r[3] = r[4];
                            /*27:  */continue L8;//goto 8;
                        } else {
                            /*24:  */r[4]++;
                            /*25:  */continue L18;//goto 18;
                        }
                    } while (true);
                } else {
                    break L8;
                }
            } while (true);
            if (r[5] < lowest) {
                lowest = r[5];
                System.out.println(Integer.toBinaryString(lowest));
            }
            if (part1 == 0) {
                part1 = r[5];
            }
        } /*28:  */ while (r[5] != r[0]);//r[4] = r[5] == r[0] ? 1 : 0;

        return new Result(part1, "");
    }
    
    protected Result optimized() {
        int[] r = new int[6];
        int part1 = 0;
        int lowest = Integer.MAX_VALUE;
        // #ip 1
        /*0:  */r[5] = 123;
        do {
            /*1:  */r[5] = r[5] & 456;
        } /*2:  */while (r[5] != 72); //r[5] = r[5] == 72 ? 1 : 0;
        /*5:  */r[5] = 0;
        do {
            /*6:  */r[3] = r[5] | (1 << 16);
            /*7:  */r[5] = 521363;
            L8: do {
                /*8:  */r[4] = r[3] & 0xFF;
                /*9:  */r[5] += r[4];
                /*10:  */r[5] = r[5] & 0xFFFFFF;
                /*11:  */r[5] = r[5] * 65899;
                /*12:  */r[5] = r[5] & 0xFFFFFF;
                /*13:  */if (256 <= r[3]) { //r[4] = 256 > r[3] ? 1 : 0;
                    /*17:  */r[4] = 0;
//                    L18: do {
//                        /*18:  */r[2] = r[4] + 1;
//                        /*19:  */r[2] *= 256;
//                        /*20:  */if (r[2] > r[3]) { //r[2] = r[2] > r[3] ? 1 : 0;
//                            /*26:  */r[3] = r[4];
//                            /*27:  */continue L8;//goto 8;
//                        } else {
//                            /*24:  */r[4]++;
//                            /*25:  */continue L18;//goto 18;
//                        }
//                    } while (true);
                } else {
                    break L8;
                }
            } while (true);
            if (r[5] < lowest) {
                lowest = r[5];
                System.out.println(Integer.toBinaryString(lowest));
            }
            if (part1 == 0) {
                part1 = r[5];
            }
        } /*28:  */ while (r[5] != r[0]);//r[4] = r[5] == r[0] ? 1 : 0;

        return new Result(part1, "");
    }
}

/*
#ip 1
0:  r[5] = 123
1:  r[5] = r[5] & 456
2:  r[5] = r[5] == 72 ? 1 : 0
3:  jmp 3
4:  goto 0
5:  r[5] = 0
6:  r[3] = r[5] | 65536
7:  r[5] = 521363
8:  r[4] = r[3] & 255
9:  r[5] += r[4]
10:  r[5] = r[5] & 16777215
11:  r[5] = r[5] * 65899
12:  r[5] = r[5] & 16777215
13:  r[4] = 256 > r[3] ? 1 : 0
14:  jmp r[4]
15:  jmp 1
16:  goto 28
17:  r[4] = 0
18:  r[2] = r[4] + 1
19:  r[2] = r[2] * 256
20:  r[2] = r[2] > r[3] ? 1 : 0
21:  jmp r[2]
22:  jmp 1
23:  goto 26
24:  r[4] = r[4] + 1
25:  goto 18
26:  r[3] = r[4]
27:  goto 8
28:  r[4] = r[5] == r[0] ? 1 : 0
29:  jmp r[4]
30:  goto 5
*/