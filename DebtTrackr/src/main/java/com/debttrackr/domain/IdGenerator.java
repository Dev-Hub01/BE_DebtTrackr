package com.debttrackr.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class IdGenerator {

    private static final String PREFIX = "TXID";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String generateId() {

        String datePart = LocalDate.now().format(FORMATTER);

        // Short UUID (8 chars for readability)
        String uuidPart = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

        return PREFIX + datePart + "-" + uuidPart;
    }
}
