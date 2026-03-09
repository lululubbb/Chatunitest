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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CSVFormat_15_5Test {

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.getIgnoreEmptyLines(), "DEFAULT should ignore empty lines");
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_RFC4180() {
        CSVFormat format = CSVFormat.RFC4180;
        assertFalse(format.getIgnoreEmptyLines(), "RFC4180 should not ignore empty lines");
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_Excel() {
        CSVFormat format = CSVFormat.EXCEL;
        assertFalse(format.getIgnoreEmptyLines(), "EXCEL should not ignore empty lines");
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_TDF() {
        CSVFormat format = CSVFormat.TDF;
        assertTrue(format.getIgnoreEmptyLines(), "TDF should ignore empty lines");
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_MySQL() {
        CSVFormat format = CSVFormat.MYSQL;
        assertFalse(format.getIgnoreEmptyLines(), "MYSQL should not ignore empty lines");
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_WithIgnoreEmptyLinesTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(true);
        assertTrue(format.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreEmptyLines_WithIgnoreEmptyLinesFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreEmptyLines(false);
        assertFalse(format.getIgnoreEmptyLines());
    }
}