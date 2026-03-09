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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_3_3Test {

    @Test
    @Timeout(8000)
    public void testNewFormat() {
        char delimiter = ',';
        CSVFormat csvFormat = CSVFormat.newFormat(delimiter);
        
        assertEquals(delimiter, csvFormat.getDelimiter());
        assertNull(csvFormat.getQuoteCharacter());
        assertNull(csvFormat.getQuoteMode());
        assertNull(csvFormat.getCommentMarker());
        assertNull(csvFormat.getEscapeCharacter());
        assertFalse(csvFormat.getIgnoreSurroundingSpaces());
        assertFalse(csvFormat.getAllowMissingColumnNames());
        assertTrue(csvFormat.getIgnoreEmptyLines());
        assertNull(csvFormat.getRecordSeparator());
        assertNull(csvFormat.getNullString());
        assertNull(csvFormat.getHeader());
        assertFalse(csvFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testNewFormatWithPrivateConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        char delimiter = '\t';
        CSVFormat csvFormat = invokePrivateConstructor(delimiter, null, null, null, null, false, false, null, null, null, false, false);

        assertEquals(delimiter, csvFormat.getDelimiter());
        assertNull(csvFormat.getQuoteCharacter());
        assertNull(csvFormat.getQuoteMode());
        assertNull(csvFormat.getCommentMarker());
        assertNull(csvFormat.getEscapeCharacter());
        assertFalse(csvFormat.getIgnoreSurroundingSpaces());
        assertFalse(csvFormat.getAllowMissingColumnNames());
        assertTrue(csvFormat.getIgnoreEmptyLines());
        assertNull(csvFormat.getRecordSeparator());
        assertNull(csvFormat.getNullString());
        assertNull(csvFormat.getHeader());
        assertFalse(csvFormat.getSkipHeaderRecord());
    }

    private CSVFormat invokePrivateConstructor(char delimiter, Character quoteChar, QuoteMode quoteMode, Character commentStart,
                                                Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                                String recordSeparator, String nullString, String[] header,
                                                boolean skipHeaderRecord, boolean allowMissingColumnNames)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        try {
            Method privateConstructor = CSVFormat.class.getDeclaredMethod("CSVFormat", char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class, boolean.class);
            privateConstructor.setAccessible(true);
            return (CSVFormat) privateConstructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord, allowMissingColumnNames);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}