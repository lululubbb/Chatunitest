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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

public class CSVFormat_44_1Test {

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames() {
        // Given
        boolean allowMissingColumnNames = true;
        CSVFormat csvFormat = new CSVFormat(',', '"', null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false);

        // When
        CSVFormat result = csvFormat.withAllowMissingColumnNames(allowMissingColumnNames);

        // Then
        assertNotNull(result);
        assertEquals(allowMissingColumnNames, result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_Default() {
        // Given
        boolean allowMissingColumnNames = true;

        // Mocking the DEFAULT CSVFormat
        CSVFormat defaultCSVFormatMock = mock(CSVFormat.class);
        when(defaultCSVFormatMock.withAllowMissingColumnNames()).thenReturn(defaultCSVFormatMock);

        // When
        CSVFormat result = CSVFormat.DEFAULT.withAllowMissingColumnNames(allowMissingColumnNames);

        // Then
        assertNotNull(result);
        assertEquals(allowMissingColumnNames, result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_PrivateConstructor() throws NoSuchFieldException, IllegalAccessException {
        // Given
        boolean allowMissingColumnNames = true;

        // Mocking the DEFAULT CSVFormat
        CSVFormat defaultCSVFormatMock = mock(CSVFormat.class);
        when(defaultCSVFormatMock.withAllowMissingColumnNames()).thenReturn(defaultCSVFormatMock);

        // Accessing private constructor using reflection
        Field field = CSVFormat.class.getDeclaredField("DEFAULT");
        field.setAccessible(true);
        CSVFormat defaultFormat = (CSVFormat) field.get(null);

        // When
        assertThrows(IllegalAccessException.class, () -> defaultFormat.withAllowMissingColumnNames(allowMissingColumnNames));
    }
}