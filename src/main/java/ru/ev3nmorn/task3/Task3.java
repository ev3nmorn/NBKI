package ru.ev3nmorn.task3;

public class Task3 {

    private static void printFizzBuzz() {
        final String fizz = "Fizz",
                buzz = "Buzz",
                fizzBuzz = "FizzBuzz";

        for (int i = 1; i < 101; ++i) {
            if (i % 3 == 0 && i % 5 == 0) {
                System.out.println(fizzBuzz);
                continue;
            }
            if (i % 3 == 0) {
                System.out.println(fizz);
                continue;
            }
            if (i % 5 == 0) {
                System.out.println(buzz);
                continue;
            }

            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        printFizzBuzz();
    }
}
