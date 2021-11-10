package ru.ev3nmorn.task4;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class CsvUtil {

    private static final String[] HEADERS = {"FID", "SERIAL_NUM", "MEMBER_CODE", "ACCT_TYPE", "OPENED_DT", "ACCT_RTE_CDE", "REPORTING_DT", "CREDIT_LIMIT"};
    private static final String DELIMITER = ";";
    private static final String SORTED_FILE = "sorted";
    private static final String BIG_SORTED_FILE = "bigSorted";
    private static final String TEMP_FILE = "temp";
    private static final String EXTENSION = ".csv";
    private static final int MAX_ENTRIES = 2;
    private static final CSVFormat csvFormat = CSVFormat.Builder.create()
            .setDelimiter(DELIMITER)
            .setHeader(HEADERS)
            .setSkipHeaderRecord(true)
            .build();

    public static void sort(String path) {
        try (FileReader reader = new FileReader(path);
             CSVPrinter printer = new CSVPrinter(new FileWriter(SORTED_FILE + EXTENSION), csvFormat)) {
            List<CSVRecord> records = csvFormat
                    .parse(reader)
                    .getRecords();
            records.sort(Comparator.comparingInt(r -> Integer.parseInt(r.get(HEADERS[0]))));
            printer.printRecord(HEADERS);
            printer.printRecords(records);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void bigDataSort(String path) {
        List<String> tempFiles = partition(path);

        merge(tempFiles, tempFiles.size() + 1);
    }

    private static void merge(List<String> files, int numTempFile) {
        if (files.size() < 2) {
            new File(files.get(0)).renameTo(new File(BIG_SORTED_FILE + EXTENSION));
            return;
        }
        List<String> newFiles = new ArrayList<>();
        for (int i = 0; i < files.size(); i += 2) {
            if (i + 1 == files.size()) {
                newFiles.add(files.get(i));
                break;
            }

            try (FileReader reader1 = new FileReader(files.get(i));
                 FileReader reader2 = new FileReader(files.get(i + 1));
                 CSVPrinter printer = new CSVPrinter(new FileWriter(TEMP_FILE + numTempFile + EXTENSION), csvFormat)) {
                newFiles.add(TEMP_FILE + numTempFile + EXTENSION);
                numTempFile++;
                printer.printRecord(HEADERS);

                Iterator<CSVRecord> it1 = csvFormat.parse(reader1).iterator(),
                        it2 = csvFormat.parse(reader2).iterator();
                CSVRecord record1 = it1.next(),
                        record2 = it2.next();

                while (true) {

                    if (Integer.parseInt(record1.get(HEADERS[0])) <
                    Integer.parseInt(record2.get(HEADERS[0]))) {
                        printer.printRecord(record1);
                        if (!it1.hasNext()) {
                            printer.printRecord(record2);
                            break;
                        }
                        record1 = it1.next();
                    } else {
                        printer.printRecord(record2);
                        if (!it2.hasNext()) {
                            printer.printRecord(record1);
                            break;
                        }
                        record2 = it2.next();
                    }

                }

                if (it1.hasNext()) {
                    writeRemaining(printer, it1);
                } else {
                    writeRemaining(printer, it2);
                }

            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }

        deleteFiles(files);
        merge(newFiles, numTempFile);
    }

    private static void writeRemaining(CSVPrinter printer, Iterator<CSVRecord> recordIterator) throws IOException {
        while (recordIterator.hasNext()) {
            printer.printRecord(recordIterator.next());
        }
    }

    private static void deleteFiles(List<String> files) {
        if (files.size() % 2 != 0) {
            files.remove(files.size() - 1);
        }

        for (String path : files) {
            new File(path).delete();
        }
    }

    private static List<String> partition(String path) {
        List<String> files = new ArrayList<>();

        try (FileReader reader = new FileReader(path)) {
            List<CSVRecord> buf = new ArrayList<>();
            int numEntries = 0,
                    tempFile = 0;
            CSVParser parser = csvFormat.parse(reader);

            for (CSVRecord record : parser) {
                if (numEntries == MAX_ENTRIES) {
                    buf.sort(Comparator.comparingInt(r -> Integer.parseInt(r.get(HEADERS[0]))));
                    tempFile++;
                    files.add(TEMP_FILE + tempFile + EXTENSION);
                    writeTempFile(tempFile, buf);

                    numEntries = 0;
                    buf.clear();
                }

                buf.add(record);
                numEntries++;
            }

            if (buf.size() > 0) {
                buf.sort(Comparator.comparingInt(r -> Integer.parseInt(r.get(HEADERS[0]))));
                tempFile++;
                files.add(TEMP_FILE + tempFile + EXTENSION);
                writeTempFile(tempFile, buf);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return files;
    }

    private static void writeTempFile(int number, List<CSVRecord> records) throws IOException {
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(TEMP_FILE + number + EXTENSION), csvFormat)) {
            printer.printRecord(HEADERS);
            printer.printRecords(records);
        }
    }
}
