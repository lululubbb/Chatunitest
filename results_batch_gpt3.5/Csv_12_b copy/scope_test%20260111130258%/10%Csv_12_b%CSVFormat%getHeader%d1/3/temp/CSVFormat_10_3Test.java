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

import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class CSVFormat_10_3Test {

    @Test
    @Timeout(8000)
    public void testGetHeader() {
        // Given
        String[] header = {"Name", "Age", "City"};
        CSVFormat csvFormat = CSVFormat.newFormat(',').withQuote('"').withHeader(header);

        // When
        String[] result = csvFormat.getHeader();

        // Then
        assertArrayEquals(header, result);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderWhenNull() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',').withQuote('"').withHeader((String[]) null);

        // When
        String[] result = csvFormat.getHeader();

        // Then
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderClone() throws Exception {
        // Given
        String[] header = {"Name", "Age", "City"};
        CSVFormat csvFormat = CSVFormat.newFormat(',').withQuote('"').withHeader(header);

        // When
        String[] result = invokePrivateMethod(csvFormat, "getHeader", String[].class);

        // Then
        assertArrayEquals(header, result);
    }

    private <T> T invokePrivateMethod(Object object, String methodName, Class<T> returnType, Object... args) throws Exception {
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        return returnType.cast(method.invoke(object, args));
    }

}