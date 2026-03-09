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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CSVFormat_64_6Test {

    @Test
    @Timeout(8000)
    public void testWithQuote() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Given
        Character quoteChar = '"';
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(',', '|', null, '#', '\\', true, false, "\r\n",
                "NULL", new Object[]{"Header"}, new String[]{"Col1", "Col2"}, true, false, false, true, false);

        // When
        CSVFormat result = csvFormat.withQuote(quoteChar);

        // Then
        assertEquals(',', result.getDelimiter());
        assertEquals(quoteChar, result.getQuoteCharacter());
        assertNull(result.getQuoteMode());
        assertEquals('#', result.getCommentMarker());
        assertEquals('\\', result.getEscapeCharacter());
        assertTrue(result.getIgnoreSurroundingSpaces());
        assertFalse(result.getIgnoreEmptyLines());
        assertEquals("\r\n", result.getRecordSeparator());
        assertEquals("NULL", result.getNullString());
        assertArrayEquals(new Object[]{"Header"}, result.getHeaderComments());
        assertArrayEquals(new String[]{"Col1", "Col2"}, result.getHeader());
        assertTrue(result.getSkipHeaderRecord());
        assertFalse(result.getAllowMissingColumnNames());
        assertFalse(result.getIgnoreHeaderCase());
        assertTrue(result.getTrim());
        assertFalse(result.getTrailingDelimiter());
    }
}