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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class CSVFormat_35_2Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLines() {
        // Given
        CSVFormat csvFormat = createCSVFormat(',', '\"', null, null, null, false, true, "\r\n", null, null, false, false);
        boolean ignoreEmptyLines = true;

        // When
        CSVFormat newCsvFormat = csvFormat.withIgnoreEmptyLines(ignoreEmptyLines);

        // Then
        assertEquals(ignoreEmptyLines, newCsvFormat.getIgnoreEmptyLines());
        assertNotSame(csvFormat, newCsvFormat);
    }

    @Test
    @Timeout(8000)
    public void testDefaultValues() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // Then
        assertEquals(',', csvFormat.getDelimiter());
        assertEquals('\"', csvFormat.getQuoteCharacter());
        assertNull(csvFormat.getQuoteMode());
        assertNull(csvFormat.getCommentMarker());
        assertNull(csvFormat.getEscapeCharacter());
        assertFalse(csvFormat.getIgnoreSurroundingSpaces());
        assertTrue(csvFormat.getIgnoreEmptyLines());
        assertEquals("\r\n", csvFormat.getRecordSeparator());
        assertNull(csvFormat.getNullString());
        assertNull(csvFormat.getHeader());
        assertFalse(csvFormat.getSkipHeaderRecord());
        assertFalse(csvFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testRFC4180() {
        // Given
        CSVFormat csvFormat = CSVFormat.RFC4180;

        // Then
        assertFalse(csvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testExcel() {
        // Given
        CSVFormat csvFormat = CSVFormat.EXCEL;

        // Then
        assertFalse(csvFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    public void testTDF() {
        // Given
        CSVFormat csvFormat = CSVFormat.TDF;

        // Then
        assertEquals('\t', csvFormat.getDelimiter());
        assertTrue(csvFormat.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testMYSQL() {
        // Given
        CSVFormat csvFormat = CSVFormat.MYSQL;

        // Then
        assertEquals('\t', csvFormat.getDelimiter());
        assertEquals('\\', csvFormat.getEscapeCharacter().charValue());
        assertFalse(csvFormat.getIgnoreEmptyLines());
        assertNull(csvFormat.getQuoteCharacter());
        assertEquals("\n", csvFormat.getRecordSeparator());
    }

    private CSVFormat createCSVFormat(char delimiter, char quoteCharacter, QuoteMode quoteMode, Character commentMarker,
                                      Character escapeCharacter, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                      String recordSeparator, String nullString, String[] header, boolean skipHeaderRecord,
                                      boolean allowMissingColumnNames) {
        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                    Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class,
                    boolean.class, boolean.class);
            constructor.setAccessible(true);
            return constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                    ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord,
                    allowMissingColumnNames);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }
}