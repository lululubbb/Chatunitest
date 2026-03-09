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

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_22_1Test {

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_DefaultFalse() {
        CSVFormat format = CSVFormat.DEFAULT;
        // By default skipHeaderRecord is false
        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_WithTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertTrue(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_ReflectionAccess() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);

        // Verify original value is true
        assertTrue(format.getSkipHeaderRecord());

        // Create a new instance with skipHeaderRecord = false
        CSVFormat formatFalse = format.withSkipHeaderRecord(false);
        assertFalse(formatFalse.getSkipHeaderRecord());

        // Use reflection to read the private field to confirm its value matches
        boolean reflectedValueFalse = skipHeaderRecordField.getBoolean(formatFalse);
        assertFalse(reflectedValueFalse);

        // Similarly, check original instance still true
        boolean reflectedValueTrue = skipHeaderRecordField.getBoolean(format);
        assertTrue(reflectedValueTrue);

        // For completeness, check setting true again
        CSVFormat formatTrue = formatFalse.withSkipHeaderRecord(true);
        assertTrue(formatTrue.getSkipHeaderRecord());
        boolean reflectedValueTrueAgain = skipHeaderRecordField.getBoolean(formatTrue);
        assertTrue(reflectedValueTrueAgain);
    }
}