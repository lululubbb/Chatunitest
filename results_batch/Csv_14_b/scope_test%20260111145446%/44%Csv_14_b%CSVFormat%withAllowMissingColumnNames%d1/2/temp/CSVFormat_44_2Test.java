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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_44_2Test {

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
        assertEquals(allowMissingColumnNames, result.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_Default() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Given
        boolean allowMissingColumnNames = true;

        // Mock CSVFormat
        CSVFormat csvFormatMock = Mockito.mock(CSVFormat.class);
        Mockito.when(csvFormatMock.getAllowMissingColumnNames()).thenReturn(allowMissingColumnNames);

        // When
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat result = constructor.newInstance(',', '"', null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false);
        result = result.withAllowMissingColumnNames(allowMissingColumnNames);

        // Then
        assertEquals(allowMissingColumnNames, result.getAllowMissingColumnNames());
    }
}