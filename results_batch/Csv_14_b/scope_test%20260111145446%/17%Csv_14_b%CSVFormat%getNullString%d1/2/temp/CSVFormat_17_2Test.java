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

import java.lang.reflect.Field;

public class CSVFormat_17_2Test {

    @Test
    @Timeout(8000)
    public void testGetNullString() {
        // Given
        CSVFormat csvFormat = new CSVFormat(',', '"', null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false);
        String expectedNullString = "\\N";

        // When
        String actualNullString = csvFormat.getNullString();

        // Then
        assertEquals(expectedNullString, actualNullString);
    }

    @Test
    @Timeout(8000)
    public void testGetNullStringWithMock() throws NoSuchFieldException, IllegalAccessException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        String expectedNullString = "\\N";

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        nullStringField.set(csvFormat, expectedNullString);

        CSVFormat mockedCsvFormat = mock(CSVFormat.class);
        when(mockedCsvFormat.getNullString()).thenReturn(expectedNullString);

        // When
        String actualNullString = mockedCsvFormat.getNullString();

        // Then
        assertEquals(expectedNullString, actualNullString);
    }
}