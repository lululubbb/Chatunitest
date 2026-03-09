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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class CSVFormat_68_6Test {

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecord() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withSkipHeaderRecord();

        // Then
        assertTrue(result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordFalse() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT.withSkipHeaderRecord(false);

        // When
        CSVFormat result = csvFormat.withSkipHeaderRecord();

        // Then
        assertFalse(result.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordTwice() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result1 = csvFormat.withSkipHeaderRecord();
        CSVFormat result2 = result1.withSkipHeaderRecord();

        // Then
        assertTrue(result1.getSkipHeaderRecord());
        assertTrue(result2.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithSkipHeaderRecordUsingReflection() throws Exception {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat csvFormatSpy = spy(csvFormat);

        // When
        CSVFormat result = null;
        try {
            result = (CSVFormat) invokePrivateMethod(csvFormatSpy, "withSkipHeaderRecord");
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }

        // Then
        assertTrue(result.getSkipHeaderRecord());
    }

    private Object invokePrivateMethod(Object object, String methodName) throws Exception {
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(object);
    }
}