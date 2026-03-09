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
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_31_6Test {

    @Test
    @Timeout(8000)
    public void testWithEscape() {
        // Given
        char escapeChar = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withEscape(escapeChar);

        // Then
        assertEquals(escapeChar, newCsvFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeUsingReflection() throws Exception {
        // Given
        char escapeChar = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = invokeWithEscapeUsingReflection(csvFormat, escapeChar);

        // Then
        assertEquals(escapeChar, newCsvFormat.getEscapeCharacter());
    }

    private CSVFormat invokeWithEscapeUsingReflection(CSVFormat csvFormat, char escapeChar) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        method.setAccessible(true);
        return (CSVFormat) method.invoke(csvFormat, escapeChar);
    }
}