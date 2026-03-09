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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_11_4Test {

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withAllowMissingColumnNames(true);

        // When
        boolean result = csvFormat.getAllowMissingColumnNames();

        // Then
        assertEquals(true, result);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNamesFalse() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withAllowMissingColumnNames(false);

        // When
        boolean result = csvFormat.getAllowMissingColumnNames();

        // Then
        assertEquals(false, result);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNamesUsingReflection() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('\"')
                .withAllowMissingColumnNames(true);

        // When
        boolean result;
        try {
            Method method = CSVFormat.class.getDeclaredMethod("getAllowMissingColumnNames");
            method.setAccessible(true);
            result = (boolean) method.invoke(csvFormat);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            result = false;
        }

        // Then
        assertEquals(true, result);
    }
}