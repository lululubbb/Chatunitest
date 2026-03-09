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

public class CSVFormat_11_2Test {

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);

        // Using reflection to access the private field
        Method method = CSVFormat.class.getDeclaredMethod("getAllowMissingColumnNames");
        method.setAccessible(true);

        // When
        boolean result = (boolean) method.invoke(csvFormat);

        // Then
        assertEquals(true, result);
    }
}