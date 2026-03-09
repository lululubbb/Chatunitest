package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_60_2Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpaces() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces();

        // Then
        assertTrue(result.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpaces_Custom() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces(true);

        // Then
        assertTrue(result.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpaces_Null() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(true);

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces(false);

        // Then
        assertFalse(result.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpaces_PrivateMethodInvocation() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat csvFormatSpy = Mockito.spy(csvFormat);

        // When
        boolean isLineBreak = (boolean) invokePrivateMethod(csvFormatSpy, "isLineBreak", 'c');

        // Then
        assertFalse(isLineBreak);
    }

    private Object invokePrivateMethod(Object object, String methodName, Object... params) throws Exception {
        Class<?>[] paramTypes = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            paramTypes[i] = params[i].getClass();
        }
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(object, params);
    }
}