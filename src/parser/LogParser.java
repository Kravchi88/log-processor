package parser;

import model.LogEntry;
import model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogParser {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<LogEntry> parseLine(String line) {
        List<LogEntry> entries = new ArrayList<>();

        if (line == null || line.trim().isEmpty()) {
            return entries;
        }

        String timestampStr = line.substring(1, line.indexOf("]"));
        LocalDateTime timestamp = LocalDateTime.parse(timestampStr, FORMATTER);

        String[] parts = line.substring(line.indexOf("]") + 2).split(" ");

        String user = parts[0];

        if (line.contains("balance inquiry")) {
            BigDecimal amount = new BigDecimal(parts[3]);

            entries.add(new LogEntry(
                    timestamp,
                    user,
                    OperationType.BALANCE_INQUIRY,
                    amount,
                    null
            ));

        } else if (line.contains("transferred") && line.contains("to")) {
            BigDecimal amount = new BigDecimal(parts[2]);
            String recipient = parts[4];

            entries.add(new LogEntry(
                    timestamp,
                    user,
                    OperationType.TRANSFERRED,
                    amount,
                    recipient
            ));

            entries.add(new LogEntry(
                    timestamp,
                    recipient,
                    OperationType.RECEIVED,
                    amount,
                    user
            ));

        } else if (line.contains("withdrew")) {
            BigDecimal amount = new BigDecimal(parts[2]);

            entries.add(new LogEntry(
                    timestamp,
                    user,
                    OperationType.WITHDREW,
                    amount,
                    null
            ));
        }
        return entries;
    }
}