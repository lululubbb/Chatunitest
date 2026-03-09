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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_12_6Test {

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_WithTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        assertTrue(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_WithFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);
        assertFalse(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreSurroundingSpaces_PredefinedFormats() {
        assertFalse(CSVFormat.RFC4180.getIgnoreSurroundingSpaces());
        assertFalse(CSVFormat.EXCEL.getIgnoreSurroundingSpaces());
        assertTrue(CSVFormat.TDF.getIgnoreSurroundingSpaces());
        assertFalse(CSVFormat.MYSQL.getIgnoreSurroundingSpaces());
    }
}