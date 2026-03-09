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

import java.lang.reflect.Method;

public class CSVFormat_39_5Test {

    @Test
    @Timeout(8000)
    public void testWithRecordSeparatorString() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Test normal string record separator
        String newSeparator = "\n";
        CSVFormat modified = original.withRecordSeparator(newSeparator);
        assertNotSame(original, modified);
        assertEquals(newSeparator, modified.getRecordSeparator());
        // Original should remain unchanged
        assertEquals("\r\n", original.getRecordSeparator());

        // Test null record separator
        CSVFormat modifiedNull = original.withRecordSeparator((String) null);
        assertNotSame(original, modifiedNull);
        assertNull(modifiedNull.getRecordSeparator());

        // Test empty string record separator
        CSVFormat modifiedEmpty = original.withRecordSeparator("");
        assertNotSame(original, modifiedEmpty);
        assertEquals("", modifiedEmpty.getRecordSeparator());

        // Use reflection to invoke getRecordSeparator to verify internal state
        Method getRecordSeparator = CSVFormat.class.getMethod("getRecordSeparator");
        assertEquals(newSeparator, getRecordSeparator.invoke(modified));
        assertNull(getRecordSeparator.invoke(modifiedNull));
        assertEquals("", getRecordSeparator.invoke(modifiedEmpty));
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparatorChar() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Test withRecordSeparator(char)
        char sepChar = '\r';
        CSVFormat modified = original.withRecordSeparator(sepChar);
        assertNotSame(original, modified);
        assertEquals(String.valueOf(sepChar), modified.getRecordSeparator());
        // Original remains unchanged
        assertEquals("\r\n", original.getRecordSeparator());

        // Use reflection to invoke getRecordSeparator to verify internal state
        Method getRecordSeparator = CSVFormat.class.getMethod("getRecordSeparator");
        assertEquals(String.valueOf(sepChar), getRecordSeparator.invoke(modified));
    }
}