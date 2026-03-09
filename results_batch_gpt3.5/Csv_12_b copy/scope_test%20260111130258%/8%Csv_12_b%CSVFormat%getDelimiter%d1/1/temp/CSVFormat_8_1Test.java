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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_8_1Test {

    @Test
    @Timeout(8000)
    public void testGetDelimiter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        char expectedDelimiter = ',';
        CSVFormat csvFormat = getCSVFormat(expectedDelimiter);

        // When
        Method method = CSVFormat.class.getDeclaredMethod("getDelimiter");
        method.setAccessible(true);
        char actualDelimiter = (char) method.invoke(csvFormat);

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    private CSVFormat getCSVFormat(char delimiter) {
        try {
            return (CSVFormat) CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                    Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class,
                    boolean.class, boolean.class).newInstance(delimiter, null, null, null, null, false, true, Constants.CRLF,
                    null, null, false, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}