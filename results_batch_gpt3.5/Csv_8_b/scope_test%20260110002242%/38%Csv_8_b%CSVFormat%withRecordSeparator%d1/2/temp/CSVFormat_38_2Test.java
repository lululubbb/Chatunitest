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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Method;

public class CSVFormat_38_2Test {

    private CSVFormat defaultFormat;

    @BeforeEach
    public void setUp() {
        defaultFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_char() {
        // Test with a normal char
        char recordSeparator = '\n';
        CSVFormat newFormat = defaultFormat.withRecordSeparator(recordSeparator);
        assertNotNull(newFormat);
        assertEquals(String.valueOf(recordSeparator), newFormat.getRecordSeparator());
        // Ensure immutability: original unchanged
        assertEquals("\r\n", defaultFormat.getRecordSeparator());

        // Test with a special char (carriage return)
        recordSeparator = '\r';
        CSVFormat newFormatCR = defaultFormat.withRecordSeparator(recordSeparator);
        assertNotNull(newFormatCR);
        assertEquals(String.valueOf(recordSeparator), newFormatCR.getRecordSeparator());

        // Test with a tab char
        recordSeparator = '\t';
        CSVFormat newFormatTab = defaultFormat.withRecordSeparator(recordSeparator);
        assertNotNull(newFormatTab);
        assertEquals(String.valueOf(recordSeparator), newFormatTab.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_String() throws Exception {
        // Using reflection to invoke public withRecordSeparator(String)
        Method withRecordSeparatorStringMethod = CSVFormat.class.getDeclaredMethod("withRecordSeparator", String.class);
        withRecordSeparatorStringMethod.setAccessible(true);

        // Test with valid string record separator
        String recordSeparator = "\n\r";
        CSVFormat newFormat = (CSVFormat) withRecordSeparatorStringMethod.invoke(defaultFormat, recordSeparator);
        assertNotNull(newFormat);
        assertEquals(recordSeparator, newFormat.getRecordSeparator());

        // Test with empty string record separator
        String emptySeparator = "";
        CSVFormat newFormatEmpty = (CSVFormat) withRecordSeparatorStringMethod.invoke(defaultFormat, emptySeparator);
        assertNotNull(newFormatEmpty);
        assertEquals(emptySeparator, newFormatEmpty.getRecordSeparator());

        // Test with null record separator
        CSVFormat newFormatNull = (CSVFormat) withRecordSeparatorStringMethod.invoke(defaultFormat, (Object) null);
        assertNotNull(newFormatNull);
        assertNull(newFormatNull.getRecordSeparator());
    }
}