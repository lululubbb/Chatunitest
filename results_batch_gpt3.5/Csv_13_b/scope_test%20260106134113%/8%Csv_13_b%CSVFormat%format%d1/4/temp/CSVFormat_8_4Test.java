package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Reader;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_8_4Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNormalValues() {
        String result = csvFormat.format("a", "b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNullValues() {
        String result = csvFormat.format("a", null, "c");
        assertEquals("a,,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithEmptyValues() {
        String result = csvFormat.format("", "", "");
        assertEquals(",,", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNoValues() {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatThrowsIllegalStateExceptionOnIOException() throws Exception {
        // We cannot subclass final CSVFormat or CSVPrinter.
        // Instead, mock CSVPrinter to throw IOException on printRecord,
        // and mock CSVFormat.print(Appendable) to return this mock printer.

        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        // Disambiguate printRecord call by casting argument to Object[]
        doThrow(new IOException("forced")).when(mockPrinter).printRecord((Object[]) any());

        CSVFormat spyFormat = spy(csvFormat);
        doReturn(mockPrinter).when(spyFormat).print(any());

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            spyFormat.format("a");
        });
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("forced", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testFormatUsesCustomCSVFormat() {
        CSVFormat customFormat = CSVFormat.DEFAULT.withDelimiter(';').withQuoteMode(QuoteMode.ALL);
        String result = customFormat.format("a;b", "c");
        // Because QuoteMode.ALL, fields should be quoted
        assertEquals("\"a;b\";\"c\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithReflectionAccessPrivateFields() throws Exception {
        // Use reflection to set private field delimiter to semicolon
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        delimiterField.set(csvFormat, ';');

        // Use reflection to set private field quoteCharacter to '"'
        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharField.setAccessible(true);
        quoteCharField.set(csvFormat, '"');

        String result = csvFormat.format("x", "y");
        assertEquals("x;y", result); // Because default quoteMode is null, quotes may not be added
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNullStringSet() throws Exception {
        // Set private field nullString to "NULL"
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        nullStringField.set(csvFormat, "NULL");

        String result = csvFormat.format(null, "test");
        // null should be replaced with nullString "NULL"
        assertTrue(result.contains("NULL"));
    }

    @Test
    @Timeout(8000)
    public void testFormatWithQuoteCharacterSetToNull() throws Exception {
        // Set quoteCharacter to null, so no quoting
        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharField.setAccessible(true);
        quoteCharField.set(csvFormat, null);

        String result = csvFormat.format("a,b", "c");
        // No quotes, delimiter is comma, so result should be "a,b,c"
        assertEquals("a,b,c", result);
    }
}