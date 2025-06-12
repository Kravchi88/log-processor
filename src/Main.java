import model.LogEntry;
import parser.LogParser;
import service.LogReader;
import service.LogWriter;
import service.TransactionProcessor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            return;
        }

        Path inputDir = Path.of(args[0]);

        try {
            List<String> rawLines = LogReader.readAllLogLines(inputDir);

            List<LogEntry> allEntries = new ArrayList<>();
            for (String line : rawLines) {
                allEntries.addAll(LogParser.parseLine(line));
            }

            Map<String, List<LogEntry>> logsByUser = TransactionProcessor.process(allEntries);

            LogWriter.writeLogs(inputDir, logsByUser);

            System.out.println("Log processing completed");
        } catch (Exception e) {
            System.err.println("Error during processing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}