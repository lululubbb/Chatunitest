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

public class CSVFormat_41_5Test {

    private CSVFormat baseFormat;

    @BeforeEach
    public void setUp() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_char_valid() {
        char recordSeparator = '\n';
        CSVFormat newFormat = baseFormat.withRecordSeparator(recordSeparator);
        assertNotNull(newFormat);
        assertEquals(String.valueOf(recordSeparator), newFormat.getRecordSeparator());
        // Original instance is immutable, so should not be the same instance
        assertNotSame(baseFormat, newFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_char_nonLineBreak() {
        // Using a char that is not a line break, e.g. semicolon
        char recordSeparator = ';';
        CSVFormat newFormat = baseFormat.withRecordSeparator(recordSeparator);
        assertNotNull(newFormat);
        assertEquals(String.valueOf(recordSeparator), newFormat.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_char_specialCharacters() {
        // Using tab character as record separator
        char recordSeparator = '\t';
        CSVFormat newFormat = baseFormat.withRecordSeparator(recordSeparator);
        assertNotNull(newFormat);
        assertEquals(String.valueOf(recordSeparator), newFormat.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_char_reflection_invocation() throws Exception {
        Method method = CSVFormat.class.getMethod("withRecordSeparator", char.class);
        Object result = method.invoke(baseFormat, '\r');
        assertNotNull(result);
        assertTrue(result instanceof CSVFormat);
        CSVFormat formatResult = (CSVFormat) result;
        assertEquals("\r", formatResult.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparator_char_multipleCalls() {
        CSVFormat format1 = baseFormat.withRecordSeparator('\n');
        CSVFormat format2 = format1.withRecordSeparator('\r');
        CSVFormat format3 = format2.withRecordSeparator('\r');
        assertEquals("\n", format1.getRecordSeparator());
        assertEquals("\r", format2.getRecordSeparator());
        assertEquals("\r", format3.getRecordSeparator());
        assertNotSame(format1, format2);
        assertSame(format2, format3);
    }
}