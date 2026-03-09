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
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

public class CSVFormat_55_1Test {

    @Test
    @Timeout(8000)
    public void testWithHeaderComments() {
        // Given
        Object[] headerComments = { "Comment 1", "Comment 2" };
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withHeaderComments(headerComments);

        // Then
        assertNotNull(result);
        assertArrayEquals(headerComments, result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderComments_Null() {
        // Given
        Object[] headerComments = null;
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withHeaderComments(headerComments);

        // Then
        assertNotNull(result);
        assertArrayEquals(headerComments, result.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderComments_PrivateConstructorInvocation() throws Exception {
        // Given
        Object[] headerComments = { "Comment 1", "Comment 2" };
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat mockCSVFormat = mock(CSVFormat.class);
        when(mockCSVFormat.withHeaderComments(headerComments)).thenCallRealMethod();

        // When
        CSVFormat result = (CSVFormat) invokePrivateConstructor(mockCSVFormat, "withHeaderComments", headerComments);

        // Then
        assertNotNull(result);
        assertArrayEquals(headerComments, result.getHeaderComments());
    }

    private Object invokePrivateConstructor(Object object, String methodName, Object... args) throws Exception {
        Class<?>[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName, argTypes);
        method.setAccessible(true);
        return method.invoke(object, args);
    }
}