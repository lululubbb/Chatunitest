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
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;

public class CSVFormat_62_4Test {

    @Test
    @Timeout(8000)
    public void testWithNullString() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withNullString("NULL");

        // Then
        assertEquals("NULL", newCsvFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testWithNullString_ReturnsNewInstance() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withNullString("NULL");

        // Then
        assertEquals(CSVFormat.class, newCsvFormat.getClass());
        assertEquals("NULL", newCsvFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testWithNullString_OriginalInstanceUnchanged() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        csvFormat.withNullString("NULL");

        // Then
        assertEquals(null, csvFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testWithNullString_NullInput() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withNullString(null);

        // Then
        assertEquals(null, newCsvFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testWithNullString_Immutable() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withNullString("NULL");

        // Then
        assertEquals(null, csvFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testWithNullString_PrivateMethodInvocation() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        String nullString = "NULL";

        // When
        CSVFormat newCsvFormat = invokePrivateMethod(csvFormat, "withNullString", String.class, nullString);

        // Then
        assertEquals(nullString, newCsvFormat.getNullString());
    }

    private <T> T invokePrivateMethod(Object obj, String methodName, Class<?> parameterType, Object argument) throws Exception {
        java.lang.reflect.Method method = obj.getClass().getDeclaredMethod(methodName, parameterType);
        method.setAccessible(true);
        return (T) method.invoke(obj, argument);
    }
}