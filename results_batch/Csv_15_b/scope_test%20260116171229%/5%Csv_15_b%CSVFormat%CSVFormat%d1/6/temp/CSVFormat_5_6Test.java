package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.SP;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.TAB;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_5_6Test {

    private Constructor<CSVFormat> constructor;

    @BeforeEach
    public void setup() throws Exception {
        constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testConstructorAndGetters() throws Exception {
        Object[] headerComments = new Object[] {"comment1", "comment2"};
        String[] header = new String[] {"col1", "col2"};
        CSVFormat format = constructor.newInstance(
                PIPE, DOUBLE_QUOTE_CHAR, QuoteMode.ALL_NON_NULL, '#', BACKSLASH,
                true, false, LF, "NULL", headerComments, header,
                true, true, true, true, true, true);

        // Check fields via getters
        assertEquals(PIPE, format.getDelimiter());
        assertEquals(DOUBLE_QUOTE_CHAR, format.getQuoteCharacter());
        assertEquals(QuoteMode.ALL_NON_NULL, format.getQuoteMode());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(Character.valueOf(BACKSLASH), format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertFalse(format.getIgnoreEmptyLines());
        assertEquals(LF, format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
        assertArrayEquals(new String[] {"comment1", "comment2"}, format.getHeaderComments());
        assertArrayEquals(header, format.getHeader());
        assertTrue(format.getSkipHeaderRecord());
        assertTrue(format.getAllowMissingColumnNames());
        assertTrue(format.getIgnoreHeaderCase());
        assertTrue(format.getTrim());
        assertTrue(format.getTrailingDelimiter());
        assertTrue(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testEqualsAndHashCode() throws Exception {
        CSVFormat f1 = CSVFormat.DEFAULT;
        CSVFormat f2 = CSVFormat.DEFAULT.withDelimiter(COMMA);
        CSVFormat f3 = CSVFormat.DEFAULT.withDelimiter(TAB);

        assertEquals(f1, f1);
        assertNotEquals(f1, null);
        assertNotEquals(f1, "string");
        assertEquals(f1.hashCode(), f1.hashCode());
        assertNotEquals(f1, f3);
        assertNotEquals(f2, f3);
    }

    @Test
    @Timeout(8000)
    public void testFormatMethod() {
        CSVFormat format = CSVFormat.DEFAULT;
        String result = format.format("a", "b", "c");
        assertEquals("a,b,c", result);

        result = format.format((Object) null);
        assertEquals("", result);

        result = format.format("a", null, "c");
        assertEquals("a,,c", result);
    }

    @Test
    @Timeout(8000)
    public void testNewFormat() {
        CSVFormat format = CSVFormat.newFormat(';');
        assertEquals(';', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testValueOf() {
        CSVFormat format = CSVFormat.valueOf("DEFAULT");
        assertEquals(CSVFormat.DEFAULT, format);

        format = CSVFormat.valueOf("EXCEL");
        assertEquals(CSVFormat.EXCEL, format);
    }

    @Test
    @Timeout(8000)
    public void testWithMethods() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        assertTrue(format.getAllowMissingColumnNames());

        format = format.withCommentMarker('#');
        assertEquals(Character.valueOf('#'), format.getCommentMarker());

        format = format.withDelimiter(';');
        assertEquals(';', format.getDelimiter());

        format = format.withEscape('\\');
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());

        format = format.withIgnoreEmptyLines(false);
        assertFalse(format.getIgnoreEmptyLines());

        format = format.withIgnoreHeaderCase(true);
        assertTrue(format.getIgnoreHeaderCase());

        format = format.withIgnoreSurroundingSpaces(true);
        assertTrue(format.getIgnoreSurroundingSpaces());

        format = format.withNullString("NULL");
        assertEquals("NULL", format.getNullString());

        format = format.withQuote('"');
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());

        format = format.withQuoteMode(QuoteMode.ALL);
        assertEquals(QuoteMode.ALL, format.getQuoteMode());

        format = format.withRecordSeparator("\n");
        assertEquals("\n", format.getRecordSeparator());

        format = format.withSkipHeaderRecord(true);
        assertTrue(format.getSkipHeaderRecord());

        format = format.withTrailingDelimiter(true);
        assertTrue(format.getTrailingDelimiter());

        format = format.withTrim(true);
        assertTrue(format.getTrim());

        format = format.withAutoFlush(true);
        assertTrue(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.isCommentMarkerSet());

        format = format.withCommentMarker('#');
        assertTrue(format.isCommentMarkerSet());
    }

    @Test
    @Timeout(8000)
    public void testIsEscapeCharacterSet() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.isEscapeCharacterSet());

        format = format.withEscape('\\');
        assertTrue(format.isEscapeCharacterSet());
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSet() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.isNullStringSet());

        format = format.withNullString("NULL");
        assertTrue(format.isNullStringSet());
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSet() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.isQuoteCharacterSet());

        format = format.withQuote((Character) null);
        assertFalse(format.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    public void testToString() {
        String s = CSVFormat.DEFAULT.toString();
        assertNotNull(s);
        assertTrue(s.contains("delimiter="));
    }

    @Test
    @Timeout(8000)
    public void testPrivateMethodsViaReflection() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);
        assertTrue((boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((boolean) isLineBreakChar.invoke(null, 'a'));

        Method isLineBreakCharObj = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharObj.setAccessible(true);
        assertTrue((boolean) isLineBreakCharObj.invoke(null, '\n'));
        assertTrue((boolean) isLineBreakCharObj.invoke(null, '\r'));
        assertFalse((boolean) isLineBreakCharObj.invoke(null, 'a'));
        assertFalse((boolean) isLineBreakCharObj.invoke(null, (Character) null));
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderMethods() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("A", "B");
        assertArrayEquals(new String[]{"A", "B"}, format.getHeader());

        // withHeaderEnum
        enum TestEnum {A, B}
        format = CSVFormat.DEFAULT.withHeader(TestEnum.class);
        assertArrayEquals(new String[]{"A", "B"}, format.getHeader());

        // withHeader(ResultSet)
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(2);
        when(meta.getColumnLabel(1)).thenReturn("col1");
        when(meta.getColumnLabel(2)).thenReturn("col2");
        format = CSVFormat.DEFAULT.withHeader(rs);
        assertArrayEquals(new String[]{"col1", "col2"}, format.getHeader());

        // withHeader(ResultSetMetaData)
        format = CSVFormat.DEFAULT.withHeader(meta);
        assertArrayEquals(new String[]{"col1", "col2"}, format.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderComments() {
        CSVFormat format = CSVFormat.DEFAULT.withHeaderComments("c1", "c2");
        assertArrayEquals(new String[]{"c1", "c2"}, format.getHeaderComments());
    }
}