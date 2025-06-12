package service;

import model.LogEntry;

import java.io.IOException;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class LogWriter {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void writeLogs(Path rootDir, Map<String, List<LogEntry>> logsByUser) throws IOException {
        Path outputDir = rootDir.resolve("transactions_by_users");
        Files.createDirectories(outputDir);

        for (Map.Entry<String, List<LogEntry>> entry : logsByUser.entrySet()) {
            String user = entry.getKey();
            List<LogEntry> logs = entry.getValue();

            Path userFile = outputDir.resolve(user + ".log");

            List<String> lines = logs.stream()
                    .map(LogWriter::formatEntry)
                    .toList();

            Files.write(userFile, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    private static String formatEntry(LogEntry entry) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(FORMATTER.format(entry.timestamp())).append("] ");
        sb.append(entry.user()).append(" ");

        return switch (entry.operation()) {
            case BALANCE_INQUIRY -> sb.append("balance inquiry ").append(entry.amount()).toString();
            case TRANSFERRED -> sb.append("transferred ").append(entry.amount())
                    .append(" to ").append(entry.otherUser()).toString();
            case RECEIVED -> sb.append("received ").append(entry.amount())
                    .append(" from ").append(entry.otherUser()).toString();
            case WITHDREW -> sb.append("withdrew ").append(entry.amount()).toString();
            case FINAL_BALANCE -> sb.append("final balance ").append(entry.amount()).toString();
        };
    }
}