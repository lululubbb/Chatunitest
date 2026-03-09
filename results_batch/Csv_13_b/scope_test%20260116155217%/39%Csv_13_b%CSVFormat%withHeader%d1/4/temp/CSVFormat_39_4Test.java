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

import java.lang.reflect.Method;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_39_4Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_NullMetaData_ReturnsCSVFormatWithNullHeader() throws Exception {
        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(csvFormat, (Object) null);

        assertNotNull(result);
        assertNull(result.getHeader());
        // Other properties remain the same
        assertEquals(csvFormat.getDelimiter(), result.getDelimiter());
        assertEquals(csvFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(csvFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(csvFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(csvFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(csvFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(csvFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(csvFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(csvFormat.getNullString(), result.getNullString());
        assertArrayEquals(csvFormat.getHeaderComments(), result.getHeaderComments());
        assertEquals(csvFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(csvFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(csvFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_MetaDataWithColumns_ReturnsCSVFormatWithHeaders() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("col1");
        when(metaData.getColumnLabel(2)).thenReturn("col2");
        when(metaData.getColumnLabel(3)).thenReturn("col3");

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(csvFormat, metaData);

        assertNotNull(result);
        String[] headers = result.getHeader();
        assertNotNull(headers);
        assertEquals(3, headers.length);
        assertEquals("col1", headers[0]);
        assertEquals("col2", headers[1]);
        assertEquals("col3", headers[2]);

        // Other properties remain the same
        assertEquals(csvFormat.getDelimiter(), result.getDelimiter());
        assertEquals(csvFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(csvFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(csvFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(csvFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(csvFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(csvFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(csvFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(csvFormat.getNullString(), result.getNullString());
        assertArrayEquals(csvFormat.getHeaderComments(), result.getHeaderComments());
        assertEquals(csvFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(csvFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(csvFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_MetaDataWithZeroColumns_ReturnsCSVFormatWithEmptyHeaders() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(0);

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withHeaderMethod.invoke(csvFormat, metaData);

        assertNotNull(result);
        String[] headers = result.getHeader();
        assertNotNull(headers);
        assertEquals(0, headers.length);

        // Other properties remain the same
        assertEquals(csvFormat.getDelimiter(), result.getDelimiter());
        assertEquals(csvFormat.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(csvFormat.getQuoteMode(), result.getQuoteMode());
        assertEquals(csvFormat.getCommentMarker(), result.getCommentMarker());
        assertEquals(csvFormat.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(csvFormat.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(csvFormat.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(csvFormat.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(csvFormat.getNullString(), result.getNullString());
        assertArrayEquals(csvFormat.getHeaderComments(), result.getHeaderComments());
        assertEquals(csvFormat.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(csvFormat.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(csvFormat.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_MetaDataThrowsSQLException_PropagatesException() throws Exception {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenThrow(new SQLException("Test exception"));

        Method withHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", ResultSetMetaData.class);
        withHeaderMethod.setAccessible(true);

        SQLException thrown = assertThrows(SQLException.class, () -> {
            try {
                withHeaderMethod.invoke(csvFormat, metaData);
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof SQLException) {
                    throw (SQLException) cause;
                } else {
                    throw e;
                }
            }
        });
        assertEquals("Test exception", thrown.getMessage());
    }
}