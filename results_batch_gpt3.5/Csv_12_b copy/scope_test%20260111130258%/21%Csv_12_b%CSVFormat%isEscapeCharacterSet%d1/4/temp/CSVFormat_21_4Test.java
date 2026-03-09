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

public class CSVFormat_21_4Test {

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSet() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        boolean result = invokeIsEscapeCharacterSet(csvFormat);

        // Then
        assertTrue(result);
    }

    private boolean invokeIsEscapeCharacterSet(CSVFormat csvFormat) throws Exception {
        return (boolean) invokePrivateMethod(csvFormat, "isEscapeCharacterSet");
    }

    private Object invokePrivateMethod(CSVFormat csvFormat, String methodName) throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(csvFormat);
    }
}