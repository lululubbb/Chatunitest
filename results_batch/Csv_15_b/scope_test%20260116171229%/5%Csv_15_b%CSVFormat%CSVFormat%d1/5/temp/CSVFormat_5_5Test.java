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
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.csv.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_5_5Test {

    private CSVFormat defaultFormat;

    @BeforeEach
    public void setUp() {
        defaultFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testDefaultConstants() {
        assertEquals(COMMA, CSVFormat.DEFAULT.getDelimiter());
        assertEquals(DOUBLE_QUOTE_CHAR, CSVFormat.DEFAULT.getQuoteCharacter());
        assertFalse(CSVFormat.DEFAULT.getIgnoreSurroundingSpaces());
        assertTrue(CSVFormat.DEFAULT.getIgnoreEmptyLines());
        assertEquals(CRLF, CSVFormat.DEFAULT.getRecordSeparator());
        assertNull(CSVFormat.DEFAULT.getNullString());
        assertNull(CSVFormat.DEFAULT.getCommentMarker());
        assertNull(CSVFormat.DEFAULT.getEscapeCharacter());
        assertFalse(CSVFormat.DEFAULT.getSkipHeaderRecord());
        assertFalse(CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertFalse(CSVFormat.DEFAULT.getIgnoreHeaderCase());
        assertFalse(CSVFormat.DEFAULT.getTrim());
        assertFalse(CSVFormat.DEFAULT.getTrailingDelimiter());
        assertFalse(CSVFormat.DEFAULT.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testNewFormat() {
        CSVFormat format = CSVFormat.newFormat(';');
        assertEquals(';', format.getDelimiter());
        assertNull(format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testValueOfKnown() {
        CSVFormat format = CSVFormat.valueOf("DEFAULT");
        assertNotNull(format);
        assertEquals(CSVFormat.DEFAULT, format);
    }

    @Test
    @Timeout(8000)
    public void testValueOfUnknownThrows() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("UNKNOWN_FORMAT"));
    }

    @Test
    @Timeout(8000)
    public void testEqualsAndHashCode() throws Exception {
        CSVFormat format1 = CSVFormat.DEFAULT.withDelimiter(';');
        CSVFormat format2 = CSVFormat.DEFAULT.withDelimiter(';');
        CSVFormat format3 = CSVFormat.DEFAULT.withDelimiter(',');
        assertEquals(format1, format2);
        assertEquals(format1.hashCode(), format2.hashCode());
        assertNotEquals(format1, format3);
    }

    @Test
    @Timeout(8000)
    public void testWithMethods() {
        CSVFormat format = CSVFormat.DEFAULT
                .withAllowMissingColumnNames(true)
                .withCommentMarker('#')
                .withDelimiter(';')
                .withEscape('\\')
                .withIgnoreEmptyLines(false)
                .withIgnoreHeaderCase(true)
                .withIgnoreSurroundingSpaces(true)
                .withNullString("NULL")
                .withQuote('"')
                .withQuoteMode(QuoteMode.ALL_NON_NULL)
                .withRecordSeparator("\n")
                .withSkipHeaderRecord(true)
                .withTrailingDelimiter(true)
                .withTrim(true)
                .withAutoFlush(true);

        assertTrue(format.getAllowMissingColumnNames());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(';', format.getDelimiter());
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());
        assertFalse(format.getIgnoreEmptyLines());
        assertTrue(format.getIgnoreHeaderCase());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertEquals("NULL", format.getNullString());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertEquals(QuoteMode.ALL_NON_NULL, format.getQuoteMode());
        assertEquals("\n", format.getRecordSeparator());
        assertTrue(format.getSkipHeaderRecord());
        assertTrue(format.getTrailingDelimiter());
        assertTrue(format.getTrim());
        assertTrue(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter() throws Exception {
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        assertTrue((Boolean) isLineBreakCharacter.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));
    }

    @Test
    @Timeout(8000)
    public void testPrivateConstructorViaReflection() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class,
                QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(',', '"', QuoteMode.ALL, '#', '\\',
                true, true, "\n", "NULL",
                new Object[] {"comment"}, new String[] {"header"},
                true, true, true, true, true, true);

        assertEquals(',', format.getDelimiter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
        assertEquals(QuoteMode.ALL, format.getQuoteMode());
        assertEquals(Character.valueOf('#'), format.getCommentMarker());
        assertEquals(Character.valueOf('\\'), format.getEscapeCharacter());
        assertTrue(format.getIgnoreSurroundingSpaces());
        assertTrue(format.getIgnoreEmptyLines());
        assertEquals("\n", format.getRecordSeparator());
        assertEquals("NULL", format.getNullString());
        assertArrayEquals(new String[] {"comment"}, format.getHeaderComments());
        assertArrayEquals(new String[] {"header"}, format.getHeader());
        assertTrue(format.getSkipHeaderRecord());
        assertTrue(format.getAllowMissingColumnNames());
        assertTrue(format.getIgnoreHeaderCase());
        assertTrue(format.getTrim());
        assertTrue(format.getTrailingDelimiter());
        assertTrue(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    public void testToStringArrayPrivateMethod() throws Exception {
        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        Object[] input = new Object[] {"a", null, 123};
        String[] result = (String[]) toStringArray.invoke(CSVFormat.DEFAULT, (Object) input);
        assertArrayEquals(new String[] {"a", null, "123"}, result);

        String[] nullResult = (String[]) toStringArray.invoke(CSVFormat.DEFAULT, (Object) null);
        assertNull(nullResult);

        String[] emptyResult = (String[]) toStringArray.invoke(CSVFormat.DEFAULT, (Object) new Object[0]);
        assertArrayEquals(new String[0], emptyResult);
    }

    @Test
    @Timeout(8000)
    public void testTrimPrivateMethod() throws Exception {
        Method trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trimMethod.setAccessible(true);

        CharSequence input = "  abc  ";
        CharSequence trimmed = (CharSequence) trimMethod.invoke(CSVFormat.DEFAULT, input);
        assertEquals("abc", trimmed.toString());

        CharSequence nullInput = null;
        CharSequence nullOutput = (CharSequence) trimMethod.invoke(CSVFormat.DEFAULT, nullInput);
        assertNull(nullOutput);

        CharSequence noTrim = "abc";
        CharSequence noTrimOutput = (CharSequence) trimMethod.invoke(CSVFormat.DEFAULT, noTrim);
        assertEquals("abc", noTrimOutput.toString());
    }

    @Test
    @Timeout(8000)
    public void testValidatePrivateMethod() throws Exception {
        Method validate = CSVFormat.class.getDeclaredMethod("validate");
        validate.setAccessible(true);

        // Should not throw for default
        validate.invoke(CSVFormat.DEFAULT);

        // Create invalid instance with invalid delimiter (line break)
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class,
                QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        // delimiter as '\n' is invalid
        CSVFormat invalidFormat = constructor.newInstance('\n', null, null, null, null,
                false, false, CRLF, null, null, null,
                false, false, false, false, false, false);

        Exception ex = assertThrows(InvocationTargetException.class, () -> validate.invoke(invalidFormat));
        assertTrue(ex.getCause() instanceof IllegalArgumentException);
    }
}