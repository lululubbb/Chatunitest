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

public class CSVFormat_31_2Test {

    @Test
    @Timeout(8000)
    public void testWithEscape() {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withEscape(escape);

        // Then
        assertNotNull(result);
        assertNotSame(csvFormat, result);
        assertEquals(escape, result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_WhenCharacterIsNull() {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withEscape(null);

        // Then
        assertNotNull(result);
        assertNotSame(csvFormat, result);
        assertNull(result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_PrivateMethodInvocation() throws Exception {
        // Given
        char escape = '\\';
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat spyCsvFormat = spy(csvFormat);

        // When
        Method method = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        method.setAccessible(true);
        CSVFormat result = (CSVFormat) method.invoke(spyCsvFormat, Character.valueOf(escape));

        // Then
        assertNotNull(result);
        assertNotSame(csvFormat, result);
        assertEquals(escape, result.getEscapeCharacter());
    }
}