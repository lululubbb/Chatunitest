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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class CSVFormat_22_4Test {

    @Test
    @Timeout(8000)
    void testIsQuoting_whenQuoteCharIsNotNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.isQuoting());
    }

    @Test
    @Timeout(8000)
    void testIsQuoting_whenQuoteCharIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to set the private final field quoteChar to null
        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);

        // Remove final modifier on the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(quoteCharField, quoteCharField.getModifiers() & ~Modifier.FINAL);

        // Set the quoteChar to null
        quoteCharField.set(format, null);

        assertFalse(format.isQuoting());
    }
}