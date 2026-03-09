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
import java.lang.reflect.Constructor;

public class CSVFormat_31_2Test {

    @Test
    @Timeout(8000)
    public void testWithHeader_NewHeader() throws Exception {
        // Arrange
        CSVFormat original = CSVFormat.DEFAULT;

        // Act
        CSVFormat result = original.withHeader("col1", "col2", "col3");

        // Assert
        assertNotNull(result);
        assertNotSame(original, result);
        assertArrayEquals(new String[]{"col1", "col2", "col3"}, result.getHeader());

        // Original header remains null
        assertNull(original.getHeader());

        // All other fields equal
        assertEquals(original.getDelimiter(), result.getDelimiter());
        assertEquals(original.getQuoteChar(), result.getQuoteChar());
        assertEquals(original.getQuotePolicy(), result.getQuotePolicy());
        assertEquals(original.getCommentStart(), result.getCommentStart());
        assertEquals(original.getEscape(), result.getEscape());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());
        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_EmptyHeader() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat result = original.withHeader();

        assertNotNull(result);
        assertNotSame(original, result);
        assertArrayEquals(new String[0], result.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_NullHeader() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat result = original.withHeader((String[]) null);

        assertNotNull(result);
        assertNotSame(original, result);
        assertNull(result.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_PrivateConstructorViaReflection() throws Exception {
        // Using reflection to invoke private constructor and then withHeader method to verify consistency

        Class<CSVFormat> clazz = CSVFormat.class;
        Class<?>[] declaredClasses = clazz.getDeclaredClasses();
        Class<?> quotePolicyClass = null;
        for (Class<?> innerClass : declaredClasses) {
            if ("Quote".equals(innerClass.getSimpleName())) {
                quotePolicyClass = innerClass;
                break;
            }
        }
        assertNotNull(quotePolicyClass, "Quote inner class not found");

        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(char.class, Character.class,
                quotePolicyClass, Character.class, Character.class, boolean.class, boolean.class,
                String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        String[] initialHeader = new String[]{"a", "b"};
        CSVFormat instance = constructor.newInstance(',', '"', null, null, null,
                false, true, "\r\n", null, initialHeader, false);

        // Call withHeader on this instance
        CSVFormat newFormat = instance.withHeader("x", "y");

        assertNotNull(newFormat);
        assertArrayEquals(new String[]{"x", "y"}, newFormat.getHeader());
        assertArrayEquals(initialHeader, instance.getHeader());
    }
}