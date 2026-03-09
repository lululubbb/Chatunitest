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

public class CSVFormat_31_1Test {

    @Test
    @Timeout(8000)
    public void testWithEscape() {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedFormat = csvFormat.withEscape(escape);

        // Then
        assertNotSame(csvFormat, updatedFormat);
        assertEquals(escape, updatedFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeUsingReflection() throws Exception {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat csvFormatSpy = spy(csvFormat);

        // When
        Method method = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        method.setAccessible(true);
        CSVFormat updatedFormat = (CSVFormat) method.invoke(csvFormatSpy, Character.valueOf(escape));

        // Then
        assertNotSame(csvFormat, updatedFormat);
        assertEquals(escape, updatedFormat.getEscapeCharacter());
    }
}