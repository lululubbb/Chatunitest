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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_31_4Test {

    @Test
    @Timeout(8000)
    public void testWithEscape() {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withEscape(escape);

        // Then
        assertEquals(Character.valueOf(escape), result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_usingReflection() throws Exception {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat csvFormatSpy = mock(CSVFormat.class);
        when(csvFormatSpy.withEscape(Character.valueOf(escape))).thenReturn(csvFormatSpy);

        // When
        Method withEscapeMethod = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        withEscapeMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withEscapeMethod.invoke(csvFormatSpy, Character.valueOf(escape));

        // Then
        assertEquals(Character.valueOf(escape), result.getEscapeCharacter());
    }
}