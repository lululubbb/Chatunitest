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

public class CSVFormat_5_3Test {

    @Test
    @Timeout(8000)
    public void testPrivateConstructorAndValidate() throws Exception {
        // Access private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class);
        constructor.setAccessible(true);

        // Create instance with various parameters
        CSVFormat csvFormat = constructor.newInstance(
                ',', '"', QuoteMode.ALL, '#', '\\',
                true, true, "\n", "NULL",
                new Object[]{"comment1", "comment2"}, new String[]{"header1", "header2"},
                true, true, true, true, true);

        assertNotNull(csvFormat);

        // Just check some getters to confirm fields set correctly
        assertEquals(',', csvFormat.getDelimiter());
        assertEquals(Character.valueOf('"'), csvFormat.getQuoteCharacter());
        assertEquals(QuoteMode.ALL, csvFormat.getQuoteMode());
        assertEquals(Character.valueOf('#'), csvFormat.getCommentMarker());
        assertEquals(Character.valueOf('\\'), csvFormat.getEscapeCharacter());
        assertTrue(csvFormat.getIgnoreSurroundingSpaces());
        assertTrue(csvFormat.getIgnoreEmptyLines());
        assertEquals("\n", csvFormat.getRecordSeparator());
        assertEquals("NULL", csvFormat.getNullString());
        assertArrayEquals(new String[]{"comment1", "comment2"}, csvFormat.getHeaderComments());
        assertArrayEquals(new String[]{"header1", "header2"}, csvFormat.getHeader());
        assertTrue(csvFormat.getSkipHeaderRecord());
        assertTrue(csvFormat.getAllowMissingColumnNames());
        assertTrue(csvFormat.getIgnoreHeaderCase());
        assertTrue(csvFormat.getTrim());
        assertTrue(csvFormat.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testStaticConstants() {
        assertNotNull(CSVFormat.DEFAULT);
        assertNotNull(CSVFormat.EXCEL);
        assertNotNull(CSVFormat.INFORMIX_UNLOAD);
        assertNotNull(CSVFormat.INFORMIX_UNLOAD_CSV);
        assertNotNull(CSVFormat.MYSQL);
        assertNotNull(CSVFormat.RFC4180);
        assertNotNull(CSVFormat.TDF);
    }

    @Test
    @Timeout(8000)
    public void testNewFormatAndValueOf() {
        CSVFormat format = CSVFormat.newFormat(';');
        assertEquals(';', format.getDelimiter());

        CSVFormat defaultFormat = CSVFormat.valueOf("DEFAULT");
        assertEquals(CSVFormat.DEFAULT, defaultFormat);
    }

    @Test
    @Timeout(8000)
    public void testEqualsAndHashCode() {
        CSVFormat format1 = CSVFormat.DEFAULT.withDelimiter(';');
        CSVFormat format2 = CSVFormat.DEFAULT.withDelimiter(';');
        CSVFormat format3 = CSVFormat.DEFAULT.withDelimiter(',');

        assertEquals(format1, format2);
        assertNotEquals(format1, format3);
        assertEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testFormatMethod() {
        CSVFormat format = CSVFormat.DEFAULT;
        String formatted = format.format("a", "b", "c");
        assertNotNull(formatted);
        assertTrue(formatted.contains("a"));
        assertTrue(formatted.contains("b"));
        assertTrue(formatted.contains("c"));
    }

    @Test
    @Timeout(8000)
    public void testWithMethodsReturnNewInstance() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat changed = base.withDelimiter(';').withQuote('\'').withIgnoreEmptyLines(false)
                .withAllowMissingColumnNames(true).withIgnoreHeaderCase(true).withTrim(true)
                .withTrailingDelimiter(true).withSkipHeaderRecord(true).withNullString("null")
                .withCommentMarker('#').withEscape('\\').withQuoteMode(QuoteMode.MINIMAL)
                .withRecordSeparator("\r\n").withHeader("h1", "h2").withHeaderComments("c1", "c2");

        assertNotSame(base, changed);
        assertEquals(';', changed.getDelimiter());
        assertEquals(Character.valueOf('\''), changed.getQuoteCharacter());
        assertFalse(changed.getIgnoreEmptyLines());
        assertTrue(changed.getAllowMissingColumnNames());
        assertTrue(changed.getIgnoreHeaderCase());
        assertTrue(changed.getTrim());
        assertTrue(changed.getTrailingDelimiter());
        assertTrue(changed.getSkipHeaderRecord());
        assertEquals("null", changed.getNullString());
        assertEquals(Character.valueOf('#'), changed.getCommentMarker());
        assertEquals(Character.valueOf('\\'), changed.getEscapeCharacter());
        assertEquals(QuoteMode.MINIMAL, changed.getQuoteMode());
        assertEquals("\r\n", changed.getRecordSeparator());
        assertArrayEquals(new String[]{"h1", "h2"}, changed.getHeader());
        assertArrayEquals(new String[]{"c1", "c2"}, changed.getHeaderComments());
    }

}