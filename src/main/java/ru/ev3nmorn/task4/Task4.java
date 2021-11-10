package ru.ev3nmorn.task4;

public class Task4 {

    public static void main(String[] args) {
        final String sortPath = "sort.csv",
                bigSortPath = "bigDataSort.csv";
        CsvUtil.sort(sortPath);
        CsvUtil.bigDataSort(bigSortPath);
    }
}
