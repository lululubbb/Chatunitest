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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_23_2Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setup() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testGetTrim() {
        // Given
        boolean expected = false;

        // When
        boolean result = csvFormat.getTrim();

        // Then
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    public void testGetTrim_WhenTrimIsTrue() throws Exception {
        // Given
        CSVFormat csvFormatMock = mock(CSVFormat.class);
        when(csvFormatMock.getTrim()).thenCallRealMethod();
        Field field = CSVFormat.class.getDeclaredField("trim");
        field.setAccessible(true);
        field.set(csvFormatMock, true);

        // When
        boolean result = csvFormatMock.getTrim();

        // Then
        assertEquals(true, result);
    }

    @Test
    @Timeout(8000)
    public void testGetTrim_WhenTrimIsFalse() throws Exception {
        // Given
        CSVFormat csvFormatMock = mock(CSVFormat.class);
        when(csvFormatMock.getTrim()).thenCallRealMethod();
        Field field = CSVFormat.class.getDeclaredField("trim");
        field.setAccessible(true);
        field.set(csvFormatMock, false);

        // When
        boolean result = csvFormatMock.getTrim();

        // Then
        assertEquals(false, result);
    }
}