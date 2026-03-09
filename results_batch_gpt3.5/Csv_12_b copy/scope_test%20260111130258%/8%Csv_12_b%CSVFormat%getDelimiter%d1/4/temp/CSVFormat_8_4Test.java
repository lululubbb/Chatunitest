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

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_8_4Test {

    @Test
    @Timeout(8000)
    public void testGetDelimiter() throws Exception {
        // Given
        char expectedDelimiter = ',';
        CSVFormat csvFormat = CSVFormat.newFormat(expectedDelimiter);

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiterForTabDelimiter() throws Exception {
        // Given
        char expectedDelimiter = '\t';
        CSVFormat csvFormat = CSVFormat.TDF;

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiterForMySqlFormat() throws Exception {
        // Given
        char expectedDelimiter = '\t';
        CSVFormat csvFormat = CSVFormat.MYSQL;

        // When
        char actualDelimiter = csvFormat.getDelimiter();

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiterUsingReflection() throws Exception {
        // Given
        char expectedDelimiter = ',';
        CSVFormat csvFormat = CSVFormat.newFormat(expectedDelimiter);

        // When
        char actualDelimiter;
        try {
            Method method = CSVFormat.class.getDeclaredMethod("getDelimiter");
            method.setAccessible(true);
            actualDelimiter = (char) method.invoke(csvFormat);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            actualDelimiter = ' '; // Handle exception
        }

        // Then
        assertEquals(expectedDelimiter, actualDelimiter);
    }
}