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

class CSVFormat_10_6Test {

    @Test
    @Timeout(8000)
    void testGetDelimiter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_RFC4180() {
        CSVFormat format = CSVFormat.RFC4180;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_EXCEL() {
        CSVFormat format = CSVFormat.EXCEL;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_TDF() {
        CSVFormat format = CSVFormat.TDF;
        assertEquals('\t', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_MYSQL() {
        CSVFormat format = CSVFormat.MYSQL;
        assertEquals('\t', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_NewFormat() {
        CSVFormat format = CSVFormat.newFormat(';');
        assertEquals(';', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_WithDelimiterMethod() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat changed = base.withDelimiter('|');
        assertEquals('|', changed.getDelimiter());
        // Original remains unchanged
        assertEquals(',', base.getDelimiter());
    }
}