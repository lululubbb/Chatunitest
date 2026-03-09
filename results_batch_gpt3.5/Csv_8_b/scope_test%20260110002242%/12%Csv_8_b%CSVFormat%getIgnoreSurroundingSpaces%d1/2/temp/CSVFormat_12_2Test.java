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

public class CSVFormat_12_2Test {

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpaces_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpaces_WithIgnoreSurroundingSpacesTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);
        assertTrue(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpaces_WithIgnoreSurroundingSpacesFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);
        assertFalse(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpaces_TDFConstant() {
        CSVFormat format = CSVFormat.TDF;
        assertTrue(format.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreSurroundingSpaces_MySQLConstant() {
        CSVFormat format = CSVFormat.MYSQL;
        assertFalse(format.getIgnoreSurroundingSpaces());
    }
}