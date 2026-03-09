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

class CSVFormat_39_4Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparatorString() throws Exception {
        // Arrange
        CSVFormat original = CSVFormat.DEFAULT;
        String newRecordSeparator = "\n";

        // Act
        CSVFormat updated = original.withRecordSeparator(newRecordSeparator);

        // Assert
        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(newRecordSeparator, updated.getRecordSeparator());

        // Original remains unchanged
        assertEquals("\r\n", original.getRecordSeparator());

        // Test with null recordSeparator
        CSVFormat nullRecordSeparatorFormat = original.withRecordSeparator((String) null);
        assertNull(nullRecordSeparatorFormat.getRecordSeparator());

        // Reflection test: invoke public method withRecordSeparator(char) to verify fields set correctly
        Method withRecordSeparatorCharMethod = CSVFormat.class.getMethod("withRecordSeparator", char.class);
        CSVFormat reflectedFormatChar = (CSVFormat) withRecordSeparatorCharMethod.invoke(original, 'R');
        assertEquals("R", reflectedFormatChar.getRecordSeparator());

        // Reflection test: invoke public method withRecordSeparator(String) to verify fields set correctly
        Method withRecordSeparatorStringMethod = CSVFormat.class.getMethod("withRecordSeparator", String.class);
        CSVFormat reflectedFormatString = (CSVFormat) withRecordSeparatorStringMethod.invoke(original, "RS");
        assertEquals("RS", reflectedFormatString.getRecordSeparator());
    }
}