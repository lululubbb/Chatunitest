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

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

public class CSVFormat_5_2Test {

    @Test
    @Timeout(8000)
    public void testCSVFormat() throws Exception {
        // Given
        char delimiter = ',';
        Character quoteChar = '"';
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class,
                QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class,
                String.class, Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(delimiter, quoteChar, null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false);

        // When
        char actualDelimiter = csvFormat.getDelimiter();
        Character actualQuoteChar = csvFormat.getQuoteCharacter();

        // Then
        assertEquals(delimiter, actualDelimiter);
        assertEquals(quoteChar, actualQuoteChar);
    }

    @Test
    @Timeout(8000)
    public void testEquals() {
        // Given
        CSVFormat csvFormat1 = CSVFormat.DEFAULT;
        CSVFormat csvFormat2 = CSVFormat.DEFAULT;

        // Then
        assertTrue(csvFormat1.equals(csvFormat2));
    }

    @Test
    @Timeout(8000)
    public void testFormat() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        String expected = "value1,value2,value3";

        // When
        String actual = csvFormat.format("value1", "value2", "value3");

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        char expected = ',';

        // When
        char actual = csvFormat.getDelimiter();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteCharacter() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Character expected = '"';

        // When
        Character actual = csvFormat.getQuoteCharacter();

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter() {
        // Given
        char newDelimiter = ';';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedFormat = csvFormat.withDelimiter(newDelimiter);

        // Then
        assertEquals(newDelimiter, updatedFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote() {
        // Given
        char newQuoteChar = '\'';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat updatedFormat = csvFormat.withQuote(newQuoteChar);

        // Then
        assertEquals(newQuoteChar, updatedFormat.getQuoteCharacter());
    }
}