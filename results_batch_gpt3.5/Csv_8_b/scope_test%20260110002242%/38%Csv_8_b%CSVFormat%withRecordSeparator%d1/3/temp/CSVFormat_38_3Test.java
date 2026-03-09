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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CSVFormat_38_3Test {

    @Test
    @Timeout(8000)
    public void testWithRecordSeparatorChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newSeparator = '\n';

        // Call public withRecordSeparator(char) method
        CSVFormat result = original.withRecordSeparator(newSeparator);

        // It should be a new CSVFormat instance, not the same as original
        assertNotSame(original, result);

        // The record separator of the new instance should be the string value of the char
        assertEquals(String.valueOf(newSeparator), result.getRecordSeparator());

        // The original instance's record separator should remain unchanged
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), original.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    public void testWithRecordSeparatorStringViaReflection() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String newSeparator = "\r\n";

        // Access public method withRecordSeparator(String)
        Method withRecordSeparatorString = CSVFormat.class.getDeclaredMethod("withRecordSeparator", String.class);
        withRecordSeparatorString.setAccessible(true);

        CSVFormat result = (CSVFormat) withRecordSeparatorString.invoke(original, newSeparator);

        // It should be a new CSVFormat instance, not the same as original
        assertNotSame(original, result);

        // The record separator of the new instance should be the string passed
        assertEquals(newSeparator, result.getRecordSeparator());

        // The original instance's record separator should remain unchanged
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), original.getRecordSeparator());
    }
}