package com.tterrag.advent2018;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            for (int i = 1; i <= 25; i++) {
                if (!runDay(i)) {
                    return;
                }
            }
        } else {
            runDay(Integer.parseInt(args[0]));
        }
    }

    private static boolean runDay(int day) {
        Class<?> dayClass;
        try {
            dayClass = Class.forName(Main.class.getCanonicalName().replaceAll("Main", "days.Day" + String.format("%02d", day)));

            System.out.println("Day " + day + ": ");
            Method m = dayClass.getDeclaredMethod("main", String[].class);
            m.setAccessible(true);
            m.invoke(null, (Object) new String[0]);
            System.out.println();
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("Could not find day " + day);
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException | SecurityException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
