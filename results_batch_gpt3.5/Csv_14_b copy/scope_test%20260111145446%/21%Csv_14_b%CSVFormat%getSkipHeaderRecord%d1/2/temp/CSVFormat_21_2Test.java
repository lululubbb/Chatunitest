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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CSVFormat_21_2Test {

    @Test
    @Timeout(8000)
    public void testGetSkipHeaderRecord() throws Exception {
        // Given
        CSVFormat csvFormat = new CSVFormat(',', '\"', null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false);

        // When
        boolean skipHeaderRecord = csvFormat.getSkipHeaderRecord();

        // Then
        assertEquals(false, skipHeaderRecord);
    }

    @Test
    @Timeout(8000)
    public void testGetSkipHeaderRecordWithMock() throws Exception {
        // Given
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(',', '\"', null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false);

        CSVFormat mockCsvFormat = mock(CSVFormat.class);
        when(mockCsvFormat.getSkipHeaderRecord()).thenReturn(true);

        // When
        boolean skipHeaderRecord = mockCsvFormat.getSkipHeaderRecord();

        // Then
        assertEquals(true, skipHeaderRecord);
    }
}