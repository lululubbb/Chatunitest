package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CSVFormat_12_1Test {

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_DefaultFalse() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_TrueViaWithIgnoreSurroundingSpaces() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        assertTrue(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_FalseViaWithIgnoreSurroundingSpaces() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);
        assertFalse(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_TDFConstantTrue() {
        assertTrue(CSVFormat.TDF.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_MySQLConstantFalse() {
        assertFalse(CSVFormat.MYSQL.getIgnoreSurroundingSpaces());
    }
}