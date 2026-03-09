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

class CSVFormat_11_3Test {

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_DefaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_RFC4180Instance() {
        CSVFormat format = CSVFormat.RFC4180;
        assertFalse(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_EXCELInstance() {
        CSVFormat format = CSVFormat.EXCEL;
        assertFalse(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_TDFInstance() {
        CSVFormat format = CSVFormat.TDF;
        assertTrue(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_MYSQLInstance() {
        CSVFormat format = CSVFormat.MYSQL;
        assertFalse(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_WithIgnoreEmptyLinesTrue() {
        CSVFormat format = CSVFormat.newFormat(',').withIgnoreEmptyLines(true);
        assertTrue(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_WithIgnoreEmptyLinesFalse() {
        CSVFormat format = CSVFormat.newFormat(',').withIgnoreEmptyLines(false);
        assertFalse(format.getIgnoreEmptyLines());
    }
}