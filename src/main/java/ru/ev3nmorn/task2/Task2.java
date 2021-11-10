package ru.ev3nmorn.task2;

public class Task2 {

    public static void main(String[] args) {
        try {
            System.out.println(NumParser.toInt("12043"));

            System.out.println(NumParser.toDouble("2344.234323"));
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
        }
    }
}
