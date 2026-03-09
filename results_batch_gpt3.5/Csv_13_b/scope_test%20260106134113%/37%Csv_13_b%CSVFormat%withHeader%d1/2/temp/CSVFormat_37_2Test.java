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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_37_2Test {

    @Test
    @Timeout(8000)
    void testWithHeader_NullHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeader((String[]) null);
        assertNotNull(newFormat);
        assertNull(newFormat.getHeader());
        // Original format remains unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withHeader(new String[]{});
        assertNotNull(newFormat);
        assertArrayEquals(new String[]{}, newFormat.getHeader());
        // Original format remains unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NonEmptyHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] header = {"A", "B", "C"};
        CSVFormat newFormat = format.withHeader(header);
        assertNotNull(newFormat);
        assertArrayEquals(header, newFormat.getHeader());
        // Original format remains unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_Immutability() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] header = {"X", "Y"};
        CSVFormat newFormat = format.withHeader(header);
        // Changing original header array should not affect CSVFormat instance
        header[0] = "Z";
        assertEquals("X", newFormat.getHeader()[0]);
    }
}