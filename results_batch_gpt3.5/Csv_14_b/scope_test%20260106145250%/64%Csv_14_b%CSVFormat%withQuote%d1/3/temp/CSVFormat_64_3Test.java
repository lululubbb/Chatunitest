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

import java.lang.reflect.Method;

class CSVFormat_64_3Test {

    @Test
    @Timeout(8000)
    void testWithQuote_withValidCharacter() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char quoteChar = '\'';

        CSVFormat newFormat = baseFormat.withQuote(quoteChar);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(quoteChar), newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteMode(), newFormat.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), newFormat.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), newFormat.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), newFormat.getHeaderComments());
        assertArrayEquals(baseFormat.getHeader(), newFormat.getHeader());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), newFormat.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), newFormat.getIgnoreHeaderCase());
        assertEquals(baseFormat.getTrim(), newFormat.getTrim());
        assertEquals(baseFormat.getTrailingDelimiter(), newFormat.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withNullCharacter() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Character quoteChar = null;

        // Use the public withQuote(Character) method via reflection
        Method withQuoteMethod = CSVFormat.class.getMethod("withQuote", Character.class);
        CSVFormat newFormat = (CSVFormat) withQuoteMethod.invoke(baseFormat, quoteChar);

        assertNotNull(newFormat);
        assertNull(newFormat.getQuoteCharacter());
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withLineBreakCharacters_shouldThrow() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char[] lineBreaks = {'\n', '\r'};

        for (char lineBreak : lineBreaks) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> baseFormat.withQuote(lineBreak));
            assertEquals("The quoteChar cannot be a line break", thrown.getMessage());
        }
    }
}