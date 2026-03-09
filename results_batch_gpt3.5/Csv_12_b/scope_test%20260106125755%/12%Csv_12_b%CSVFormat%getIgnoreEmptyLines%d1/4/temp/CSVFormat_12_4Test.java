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

class CSVFormat_12_4Test {

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_DefaultTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_WithIgnoreEmptyLinesFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        assertFalse(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_WithIgnoreEmptyLinesTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        assertTrue(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_RFC4180False() {
        assertFalse(CSVFormat.RFC4180.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_EXCELFalse() {
        assertFalse(CSVFormat.EXCEL.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_TDFTrue() {
        assertTrue(CSVFormat.TDF.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreEmptyLines_MYSQLFalse() {
        assertFalse(CSVFormat.MYSQL.getIgnoreEmptyLines());
    }
}