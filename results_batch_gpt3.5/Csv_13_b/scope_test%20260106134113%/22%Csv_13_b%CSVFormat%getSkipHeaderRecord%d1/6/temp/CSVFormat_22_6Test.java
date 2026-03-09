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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_22_6Test {

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_DefaultInstance() {
        // DEFAULT instance skipHeaderRecord is false
        assertFalse(CSVFormat.DEFAULT.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_WithSkipHeaderRecordTrue() {
        // Create CSVFormat instance with skipHeaderRecord = true using withSkipHeaderRecord method
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertTrue(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_WithSkipHeaderRecordFalse() {
        // Create CSVFormat instance with skipHeaderRecord = false using withSkipHeaderRecord method
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_ReflectionDirectFieldAccess() throws Exception {
        // Use reflection to modify skipHeaderRecord field on a new CSVFormat instance (not DEFAULT, which is final)
        Class<CSVFormat> clazz = CSVFormat.class;
        Field skipHeaderRecordField = clazz.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);

        // Create a new instance by calling withSkipHeaderRecord(false) to get mutable instance
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(false);

        // Remove final modifier from the skipHeaderRecord field using reflection on the Field object
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(skipHeaderRecordField, skipHeaderRecordField.getModifiers() & ~Modifier.FINAL);

        // Set skipHeaderRecord field to true forcibly
        skipHeaderRecordField.set(format, true);
        assertTrue(format.getSkipHeaderRecord());

        // Reset to false
        skipHeaderRecordField.set(format, false);
        assertFalse(format.getSkipHeaderRecord());
    }
}