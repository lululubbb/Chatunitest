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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatWithHeaderTest {

    private CSVFormat baseFormat;

    @BeforeEach
    void setUp() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NullMetaData() throws SQLException {
        // Call withHeader(ResultSetMetaData) explicitly to avoid ambiguity
        CSVFormat result = baseFormat.withHeader((ResultSetMetaData) null);
        assertNotNull(result);
        assertNull(result.getHeader());
        // Other fields remain unchanged
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
    void testWithHeader_MetaDataWithColumns() throws SQLException {
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

        // Other fields remain unchanged
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
    void testWithHeader_MetaDataZeroColumns() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(0);

        CSVFormat result = baseFormat.withHeader(metaData);

        assertNotNull(result);
        String[] header = result.getHeader();
        assertNotNull(header);
        assertEquals(0, header.length);

        // Other fields remain unchanged
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
    void testWithHeader_MetaDataThrowsSQLException() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenThrow(new SQLException("Test Exception"));

        SQLException thrown = assertThrows(SQLException.class, () -> baseFormat.withHeader(metaData));
        assertEquals("Test Exception", thrown.getMessage());
    }
}