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

import java.lang.reflect.Field;
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
    void testWithHeader_NullMetaData_ReturnsFormatWithNullHeader() throws SQLException, NoSuchFieldException, IllegalAccessException {
        CSVFormat formatWithHeader = baseFormat.withHeader((ResultSetMetaData) null);
        assertNotNull(formatWithHeader);

        // Use reflection to access the private final header field
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(formatWithHeader);

        assertNull(header);

        // Other properties remain unchanged
        assertEquals(baseFormat.getDelimiter(), formatWithHeader.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), formatWithHeader.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), formatWithHeader.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), formatWithHeader.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), formatWithHeader.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), formatWithHeader.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), formatWithHeader.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), formatWithHeader.getRecordSeparator());
        assertArrayEquals(baseFormat.getHeaderComments(), formatWithHeader.getHeaderComments());
        assertEquals(baseFormat.getSkipHeaderRecord(), formatWithHeader.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), formatWithHeader.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), formatWithHeader.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MetaDataWithZeroColumns_ReturnsFormatWithEmptyHeader() throws SQLException, NoSuchFieldException, IllegalAccessException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(0);

        CSVFormat formatWithHeader = baseFormat.withHeader(metaData);
        assertNotNull(formatWithHeader);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(formatWithHeader);

        assertNotNull(header);
        assertEquals(0, header.length);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MetaDataWithColumns_ReturnsFormatWithCorrectHeader() throws SQLException, NoSuchFieldException, IllegalAccessException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("id");
        when(metaData.getColumnLabel(2)).thenReturn("name");
        when(metaData.getColumnLabel(3)).thenReturn("age");

        CSVFormat formatWithHeader = baseFormat.withHeader(metaData);
        assertNotNull(formatWithHeader);

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(formatWithHeader);

        assertNotNull(header);
        assertArrayEquals(new String[] {"id", "name", "age"}, header);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MetaDataThrowsSQLException_PropagatesException() throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenThrow(new SQLException("fail"));

        SQLException thrown = assertThrows(SQLException.class, () -> baseFormat.withHeader(metaData));
        assertEquals("fail", thrown.getMessage());
    }

}