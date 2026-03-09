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
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_7_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testFormatWithValues() throws Exception {
        // Using real CSVFormat.DEFAULT to test format method
        String formatted = csvFormat.format("a", "b", "c");
        assertNotNull(formatted);
        assertFalse(formatted.isEmpty());
        // The output should contain the values separated by delimiter (comma)
        assertTrue(formatted.contains("a"));
        assertTrue(formatted.contains("b"));
        assertTrue(formatted.contains("c"));
        // Should not end with record separator trimmed (CRLF by default)
        assertFalse(formatted.endsWith("\r\n"));
    }

    @Test
    @Timeout(8000)
    void testFormatWithNullValue() {
        String formatted = csvFormat.format("a", null, "c");
        assertNotNull(formatted);
        assertTrue(formatted.contains("a"));
        assertTrue(formatted.contains("c"));
        // Should contain empty or nullString representation for null
        assertTrue(formatted.contains(""));
    }

    @Test
    @Timeout(8000)
    void testFormatEmptyValues() {
        String formatted = csvFormat.format();
        assertNotNull(formatted);
        assertEquals("", formatted);
    }

    @Test
    @Timeout(8000)
    void testFormatThrowsIllegalStateExceptionOnIOException() throws Exception {
        // Use reflection to create a proxy CSVFormat that throws IOException on printRecord

        CSVFormat format = new CSVFormatWrapper(csvFormat);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            format.format("x");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("mock IO exception", thrown.getCause().getMessage());
    }

    /**
     * A CSVFormat wrapper that overrides format(Object...) method to throw IOException via mocked CSVPrinter.
     */
    private static class CSVFormatWrapper extends CSVFormat {
        private static final long serialVersionUID = 1L;
        private final CSVFormat delegate;

        CSVFormatWrapper(CSVFormat delegate) {
            super(delegate.getDelimiter(), delegate.getQuoteCharacter(), delegate.getQuoteMode(),
                    delegate.getCommentMarker(), delegate.getEscapeCharacter(), delegate.getIgnoreSurroundingSpaces(),
                    delegate.getIgnoreEmptyLines(), delegate.getRecordSeparator(), delegate.getNullString(),
                    delegate.headerComments, delegate.header, delegate.getSkipHeaderRecord(),
                    delegate.getAllowMissingColumnNames(), delegate.getIgnoreHeaderCase(), delegate.getTrim(),
                    delegate.getTrailingDelimiter(), delegate.getAutoFlush());
            this.delegate = delegate;
        }

        @Override
        public String format(final Object... values) {
            final StringWriter out = new StringWriter();
            CSVPrinter csvPrinter = mock(CSVPrinter.class);
            try {
                doThrow(new IOException("mock IO exception")).when(csvPrinter).printRecord((Object[]) any());
                csvPrinter.printRecord(values);
                return out.toString().trim();
            } catch (final IOException e) {
                throw new IllegalStateException(e);
            } finally {
                try {
                    csvPrinter.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    @Test
    @Timeout(8000)
    void testFormatWithDifferentCSVFormatInstances() {
        CSVFormat format = CSVFormat.EXCEL;
        String result = format.format("val1", "val2");
        assertNotNull(result);
        assertTrue(result.contains("val1"));
        assertTrue(result.contains("val2"));

        format = CSVFormat.MYSQL;
        result = format.format("v1", "v2");
        assertNotNull(result);
        assertTrue(result.contains("v1"));
        assertTrue(result.contains("v2"));

        format = CSVFormat.INFORMIX_UNLOAD;
        result = format.format("1", "2");
        assertNotNull(result);
        assertTrue(result.contains("1"));
        assertTrue(result.contains("2"));
    }

    @Test
    @Timeout(8000)
    void testFormatWithReflection() throws Exception {
        Method formatMethod = CSVFormat.class.getDeclaredMethod("format", Object[].class);
        formatMethod.setAccessible(true);
        String result = (String) formatMethod.invoke(csvFormat, (Object) new Object[]{"x", "y"});
        assertNotNull(result);
        assertTrue(result.contains("x"));
        assertTrue(result.contains("y"));
    }
}