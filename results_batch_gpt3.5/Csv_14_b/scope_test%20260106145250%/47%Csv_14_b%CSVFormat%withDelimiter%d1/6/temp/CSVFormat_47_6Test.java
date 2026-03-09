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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class CSVFormat_47_6Test {

    @Test
    @Timeout(8000)
    void testWithDelimiter_validDelimiter() throws Exception {
        char delimiter = ';';
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withDelimiter(delimiter);

        assertNotNull(result);
        assertEquals(delimiter, result.getDelimiter());
        // Other properties remain unchanged
        assertEquals(original.getQuoteCharacter(), result.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), result.getQuoteMode());
        assertEquals(original.getCommentMarker(), result.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), result.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), result.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), result.getRecordSeparator());
        assertEquals(original.getNullString(), result.getNullString());

        // headerComments and header can be null, so handle null safely
        Object[] originalHeaderComments = getHeaderComments(original);
        Object[] resultHeaderComments = getHeaderComments(result);
        if (originalHeaderComments == null) {
            assertNull(resultHeaderComments);
        } else {
            assertArrayEquals(originalHeaderComments, resultHeaderComments);
        }

        String[] originalHeader = original.getHeader();
        String[] resultHeader = result.getHeader();
        if (originalHeader == null) {
            assertNull(resultHeader);
        } else {
            assertArrayEquals(originalHeader, resultHeader);
        }

        assertEquals(original.getSkipHeaderRecord(), result.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), result.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), result.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), result.getTrim());
        assertEquals(original.getTrailingDelimiter(), result.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithDelimiter_lineBreakDelimiterThrows() {
        char[] lineBreaks = {'\n', '\r'};
        for (char lb : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                CSVFormat.DEFAULT.withDelimiter(lb);
            });
            assertEquals("The delimiter cannot be a line break", thrown.getMessage());
        }
    }

    private static Object[] getHeaderComments(CSVFormat format) {
        try {
            Field field = CSVFormat.class.getDeclaredField("headerComments");
            field.setAccessible(true);
            return (Object[]) field.get(format);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}