package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LogEntry(
        LocalDateTime timestamp,
        String user,
        OperationType operation,
        BigDecimal amount,
        String otherUser
) {}
