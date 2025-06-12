package service;

import model.LogEntry;
import model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class TransactionProcessor {

    public static Map<String, List<LogEntry>> process(List<LogEntry> allEntries) {
        Map<String, List<LogEntry>> userLogs = new HashMap<>();

        for (LogEntry entry : allEntries) {
            userLogs
                    .computeIfAbsent(entry.user(), u -> new ArrayList<>())
                    .add(entry);
        }

        for (Map.Entry<String, List<LogEntry>> entry : userLogs.entrySet()) {
            String user = entry.getKey();
            List<LogEntry> logs = entry.getValue();

            logs.sort(Comparator.comparing(LogEntry::timestamp));

            BigDecimal balance = BigDecimal.ZERO;

            for (LogEntry log : logs) {
                switch (log.operation()) {
                    case RECEIVED -> balance = balance.add(log.amount());
                    case TRANSFERRED, WITHDREW -> balance = balance.subtract(log.amount());
                    case BALANCE_INQUIRY -> balance = log.amount();
                }
            }

            logs.add(new LogEntry(
                    LocalDateTime.now(),
                    user,
                    OperationType.FINAL_BALANCE,
                    balance,
                    null
            ));
        }

        return userLogs;
    }
}