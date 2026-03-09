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

public class CSVFormat_21_2Test {

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals("\r\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator_RFC4180() {
        CSVFormat format = CSVFormat.RFC4180;
        assertEquals("\r\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator_EXCEL() {
        CSVFormat format = CSVFormat.EXCEL;
        assertEquals("\r\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator_TDF() {
        CSVFormat format = CSVFormat.TDF;
        // TDF uses tab delimiter but record separator remains CRLF
        assertEquals("\r\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator_MYSQL() {
        CSVFormat format = CSVFormat.MYSQL;
        assertEquals("\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator_CustomString() {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator("CUSTOM");
        assertEquals("CUSTOM", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator_CustomChar() {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator(';');
        assertEquals(";", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator_Null() {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator((String) null);
        assertNull(format.getRecordSeparator());
    }
}