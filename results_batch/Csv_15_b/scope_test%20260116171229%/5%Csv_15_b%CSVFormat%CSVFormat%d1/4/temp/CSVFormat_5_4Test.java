package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
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
import java.nio.file.Files;
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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

public class CSVFormat_5_4Test {

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructorAndDefaults() throws Exception {
        // Use reflection to access the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        // Create instance with various parameters
        CSVFormat format = constructor.newInstance(
                ';', // delimiter
                '"', // quoteChar
                QuoteMode.ALL_NON_NULL, // quoteMode
                '#', // commentStart
                '\\', // escape
                true, // ignoreSurroundingSpaces
                false, // ignoreEmptyLines
                "\n", // recordSeparator
                "NULL", // nullString
                new Object[]{"comment1", "comment2"}, // headerComments
                new String[]{"header1", "header2"}, // header
                true, // skipHeaderRecord
                true, // allowMissingColumnNames
                true, // ignoreHeaderCase
                true, // trim
                true, // trailingDelimiter
                true // autoFlush
        );

        assertEquals(';', format.getDelimiter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertEquals(QuoteMode.ALL_NON_NULL, format.getQuoteMode());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertEquals("\n", format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
        assertArrayEquals(new String[]{"comment1", "comment2"}, format.getHeaderComments());
        assertArrayEquals(new String[]{"header1", "header2"}, format.getHeader());
        assertTrue(format.getSkipHeaderRecord());
        assertTrue(format.getAllowMissingColumnNames());
        assertTrue(format.getIgnoreHeaderCase());
        assertTrue(format.getTrim());
        assertTrue(format.getTrailingDelimiter());
        assertTrue(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatConstructorWithNullsAndEmptyArrays() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
                ',', // delimiter
                null, // quoteChar
                null, // quoteMode
                null, // commentStart
                null, // escape
                false, // ignoreSurroundingSpaces
                true, // ignoreEmptyLines
                "\r\n", // recordSeparator
                null, // nullString
                new Object[0], // headerComments empty array
                null, // header null
                false, // skipHeaderRecord
                false, // allowMissingColumnNames
                false, // ignoreHeaderCase
                false, // trim
                false, // trailingDelimiter
                false // autoFlush
        );

        assertEquals(',', format.getDelimiter());
        assertNull(format.getQuoteCharacter());
        assertNull(format.getQuoteMode());
        assertNull(format.getCommentMarker());
        assertNull(format.getEscapeCharacter());
        assertFalse(format.getIgnoreSurroundingSpaces());
        assertTrue(format.getIgnoreEmptyLines());
        assertEquals("\r\n", format.getRecordSeparator());
        assertNull(format.getNullString());
        assertArrayEquals(new String[0], format.getHeaderComments());
        assertNull(format.getHeader());
        assertFalse(format.getSkipHeaderRecord());
        assertFalse(format.getAllowMissingColumnNames());
        assertFalse(format.getIgnoreHeaderCase());
        assertFalse(format.getTrim());
        assertFalse(format.getTrailingDelimiter());
        assertFalse(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testCSVFormatPredefinedConstants() {
        CSVFormat defaultFormat = CSVFormat.DEFAULT;
        assertEquals(',', defaultFormat.getDelimiter());
        assertEquals(Character.valueOf('"'), defaultFormat.getQuoteCharacter());
        assertNull(defaultFormat.getQuoteMode());
        assertNull(defaultFormat.getCommentMarker());
        assertNull(defaultFormat.getEscapeCharacter());
        assertFalse(defaultFormat.getIgnoreSurroundingSpaces());
        assertTrue(defaultFormat.getIgnoreEmptyLines());
        assertEquals("\r\n", defaultFormat.getRecordSeparator());
        assertNull(defaultFormat.getNullString());
        assertNull(defaultFormat.getHeader());
        assertFalse(defaultFormat.getSkipHeaderRecord());
        assertFalse(defaultFormat.getAllowMissingColumnNames());
        assertFalse(defaultFormat.getIgnoreHeaderCase());
        assertFalse(defaultFormat.getTrim());
        assertFalse(defaultFormat.getTrailingDelimiter());
        assertFalse(defaultFormat.getAutoFlush());

        CSVFormat excelFormat = CSVFormat.EXCEL;
        assertFalse(excelFormat.getIgnoreEmptyLines());
        assertTrue(excelFormat.getAllowMissingColumnNames());

        CSVFormat mysqlFormat = CSVFormat.MYSQL;
        assertEquals('\t', mysqlFormat.getDelimiter());
        assertEquals(Character.valueOf('\\'), mysqlFormat.getEscapeCharacter());
        assertFalse(mysqlFormat.getIgnoreEmptyLines());
        assertNull(mysqlFormat.getQuoteCharacter());
        assertEquals("\n", mysqlFormat.getRecordSeparator());
        assertEquals("\\N", mysqlFormat.getNullString());
        assertEquals(QuoteMode.ALL_NON_NULL, mysqlFormat.getQuoteMode());
    }
}