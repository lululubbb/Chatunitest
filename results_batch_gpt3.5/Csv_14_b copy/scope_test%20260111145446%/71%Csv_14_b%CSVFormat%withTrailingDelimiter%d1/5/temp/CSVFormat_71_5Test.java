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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_71_5Test {

    @Test
    @Timeout(8000)
    public void testWithTrailingDelimiter() throws NoSuchMethodException, SecurityException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        boolean trailingDelimiter = true;
        CSVFormat expected = new CSVFormat(csvFormat.getDelimiter(), csvFormat.getQuoteCharacter(),
                csvFormat.getQuoteMode(), csvFormat.getCommentMarker(), csvFormat.getEscapeCharacter(),
                csvFormat.getIgnoreSurroundingSpaces(), csvFormat.getIgnoreEmptyLines(), csvFormat.getRecordSeparator(),
                csvFormat.getNullString(), csvFormat.getHeaderComments(), csvFormat.getHeader(), csvFormat.getSkipHeaderRecord(),
                csvFormat.getAllowMissingColumnNames(), csvFormat.getIgnoreHeaderCase(), csvFormat.getTrim(), trailingDelimiter);

        // When
        Method method = CSVFormat.class.getDeclaredMethod("withTrailingDelimiter", boolean.class);
        method.setAccessible(true);
        CSVFormat result = (CSVFormat) method.invoke(csvFormat, trailingDelimiter);

        // Then
        assertEquals(expected.getDelimiter(), result.getDelimiter());
        assertEquals(expected.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(expected.getQuoteMode(), result.getQuoteMode());
        assertEquals(expected.getCommentMarker(), result.getCommentMarker());
        assertEquals(expected.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(expected.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(expected.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(expected.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(expected.getNullString(), result.getNullString());
        assertArrayEquals(expected.getHeaderComments(), result.getHeaderComments());
        assertArrayEquals(expected.getHeader(), result.getHeader());
        assertEquals(expected.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(expected.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(expected.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(expected.getTrim(), result.getTrim());
        assertEquals(expected.getTrailingDelimiter(), result.getTrailingDelimiter());
    }
}