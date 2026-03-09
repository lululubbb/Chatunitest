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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_37_3Test {

    @Test
    @Timeout(8000)
    void testWithHeader_NullHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = baseFormat.withHeader((String[]) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getHeader());
        // Original format header remains unchanged
        assertNull(baseFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = baseFormat.withHeader(new String[0]);
        assertNotNull(newFormat);
        assertNotNull(newFormat.getHeader());
        assertEquals(0, newFormat.getHeader().length);
        // Original format header remains unchanged
        assertNull(baseFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NonEmptyHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        String[] headers = {"id", "name", "email"};
        CSVFormat newFormat = baseFormat.withHeader(headers);
        assertNotNull(newFormat);
        assertArrayEquals(headers, newFormat.getHeader());
        // Original format header remains unchanged
        assertNull(baseFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_Immutability() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        String[] headers = {"col1"};
        CSVFormat newFormat = baseFormat.withHeader(headers);
        // Changing original headers array after withHeader call should not affect newFormat
        headers[0] = "changed";
        assertNotEquals(headers[0], newFormat.getHeader()[0]);
    }
}