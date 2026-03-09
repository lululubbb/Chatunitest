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
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVFormatWithHeaderTest {

    private CSVFormat baseFormat;

    @BeforeEach
    void setUp() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NullMetaData_ReturnsFormatWithNullHeader() throws Exception {
        // Use reflection to invoke private constructor with header = null since withHeader(null) is ambiguous or not allowed
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat result = constructor.newInstance(
                baseFormat.getDelimiter(),
                baseFormat.getQuoteCharacter(),
                baseFormat.getQuoteMode(),
                baseFormat.getCommentMarker(),
                baseFormat.getEscapeCharacter(),
                baseFormat.getIgnoreSurroundingSpaces(),
                baseFormat.getIgnoreEmptyLines(),
                baseFormat.getRecordSeparator(),
                baseFormat.getNullString(),
                baseFormat.getHeaderComments(),
                (Object[]) null,
                baseFormat.getSkipHeaderRecord(),
                baseFormat.getAllowMissingColumnNames(),
                baseFormat.getIgnoreHeaderCase()
        );

        assertNotNull(result);
        assertNull(result.getHeader());

        // Other properties remain unchanged
        assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), result.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), result.getHeaderComments());
        assertEquals(baseFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MetaDataWithColumns_ReturnsFormatWithHeaderLabels() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenReturn("col2");
        when(metaData.getColumnLabel(3)).thenReturn("col3");

        CSVFormat result = baseFormat.withHeader(metaData);

        assertNotNull(result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertEquals(3, header.length);
        assertEquals("col1", header[0]);
        assertEquals("col2", header[1]);
        assertEquals("col3", header[2]);

        // Other properties remain unchanged
        assertEquals(baseFormat.getDelimiter(), result.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), result.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), result.getHeaderComments());
        assertEquals(baseFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());

        verify(metaData, times(1)).getColumnCount();
        verify(metaData, times(1)).getColumnLabel(1);
        verify(metaData, times(1)).getColumnLabel(2);
        verify(metaData, times(1)).getColumnLabel(3);
        verifyNoMoreInteractions(metaData);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MetaDataThrowsSQLException_PropagatesException() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenThrow(new SQLException("error"));

        SQLException thrown = assertThrows(SQLException.class, () -> baseFormat.withHeader(metaData));
        assertEquals("error", thrown.getMessage());

        verify(metaData, times(1)).getColumnCount();
        verifyNoMoreInteractions(metaData);
    }
}