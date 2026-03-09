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

public class CSVFormat_33_1Test {

    @Test
    @Timeout(8000)
    public void testWithHeader() {
        // Given
        String[] header = {"Name", "Age", "City"};
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withHeader(header);

        // Then
        assertArrayEquals(header, result.getHeader());
        assertNotSame(csvFormat, result);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_EmptyHeader() {
        // Given
        String[] header = {};
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withHeader(header);

        // Then
        assertArrayEquals(header, result.getHeader());
        assertNotSame(csvFormat, result);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_NullHeader() {
        // Given
        String[] header = null;
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withHeader(header);

        // Then
        assertNull(result.getHeader());
        assertNotSame(csvFormat, result);
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_ReflectiveInvocation() throws Exception {
        // Given
        String[] header = {"Name", "Age", "City"};
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat instance = spy(csvFormat);

        // When
        Method method = CSVFormat.class.getDeclaredMethod("withHeader", String[].class);
        method.setAccessible(true);
        CSVFormat result = (CSVFormat) method.invoke(instance, (Object) header);

        // Then
        assertArrayEquals(header, result.getHeader());
        assertNotSame(csvFormat, result);
    }
}