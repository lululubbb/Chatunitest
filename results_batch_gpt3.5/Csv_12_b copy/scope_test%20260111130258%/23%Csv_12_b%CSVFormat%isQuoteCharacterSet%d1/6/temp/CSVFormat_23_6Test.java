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

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

public class CSVFormat_23_6Test {

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet() {
        // Given
        char delimiter = ',';
        char quoteChar = '"';
        CSVFormat csvFormat = null;
        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            csvFormat = constructor.newInstance(delimiter, quoteChar, null, null, null, false, true, "\r\n", null, null, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // When
        boolean result = csvFormat.isQuoteCharacterSet();

        // Then
        assertFalse(result);
    }
}