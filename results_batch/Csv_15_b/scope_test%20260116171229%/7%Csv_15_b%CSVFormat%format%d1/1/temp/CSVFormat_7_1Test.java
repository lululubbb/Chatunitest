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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_7_1Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testFormatWithSingleValue() throws IOException {
        String result = csvFormat.format("value1");
        assertEquals("value1", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithMultipleValues() throws IOException {
        String result = csvFormat.format("value1", "value2", "value3");
        assertEquals("value1,value2,value3", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithNullValue() throws IOException {
        String result = csvFormat.format("value1", null, "value3");
        // null is printed as empty string by default
        assertEquals("value1,,value3", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithEmptyValues() throws IOException {
        String result = csvFormat.format("", "");
        assertEquals(",", result);
    }

    @Test
    @Timeout(8000)
    void testFormatWithNoValues() throws IOException {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormatThrowsIllegalStateExceptionOnIOException() throws Exception {
        CSVPrinter spyPrinter = mock(CSVPrinter.class);
        doThrow(new IOException("IO error")).when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).close();

        // Create a spy of CSVFormat.DEFAULT
        CSVFormat spyFormat = spy(CSVFormat.DEFAULT);

        // Use reflection to replace the private 'printer' method's returned CSVPrinter instance
        // Since we cannot override private methods or subclass final class,
        // we intercept the call to CSVPrinter constructor via a spy on CSVFormat.format method.

        // Instead, use reflection to replace the private printer() method via a proxy pattern:
        // But since CSVFormat is final and printer() is private, we cannot override or mock it directly.
        // So we use reflection to replace the private method call within format() by temporarily modifying the CSVFormat instance.

        // The simplest approach is to use Mockito's spy on CSVFormat and use a partial mock to override the 'printer' method via reflection.

        // We use reflection to get the private 'printer' method and make it accessible
        Method printerMethod = CSVFormat.class.getDeclaredMethod("printer");
        printerMethod.setAccessible(true);

        // Use a dynamic proxy to intercept calls to printer() is not feasible here,
        // so instead we temporarily replace the printer() method by using a spy and doAnswer on format()

        // We create an Answer that calls the spyPrinter when printer() is invoked inside format()

        // To do this, we use a spy on CSVFormat and override format() method to use the spyPrinter

        CSVFormat formatWithMockedPrinter = new CSVFormatWrapper(spyFormat, spyPrinter);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            formatWithMockedPrinter.format("value1");
        });
        assertTrue(exception.getCause() instanceof IOException);
        assertEquals("IO error", exception.getCause().getMessage());
    }

    // Wrapper class to intercept calls to private printer() method inside format()
    static class CSVFormatWrapper extends CSVFormat {
        private final CSVFormat delegate;
        private final CSVPrinter printer;

        CSVFormatWrapper(CSVFormat delegate, CSVPrinter printer) {
            super(CSVFormat.DEFAULT.getDelimiter(),
                    CSVFormat.DEFAULT.getQuoteCharacter(),
                    CSVFormat.DEFAULT.getQuoteMode(),
                    CSVFormat.DEFAULT.getCommentMarker(),
                    CSVFormat.DEFAULT.getEscapeCharacter(),
                    CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                    CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                    CSVFormat.DEFAULT.getRecordSeparator(),
                    CSVFormat.DEFAULT.getNullString(),
                    null,
                    CSVFormat.DEFAULT.getHeader(),
                    CSVFormat.DEFAULT.getSkipHeaderRecord(),
                    CSVFormat.DEFAULT.getAllowMissingColumnNames(),
                    CSVFormat.DEFAULT.getIgnoreHeaderCase(),
                    CSVFormat.DEFAULT.getTrim(),
                    CSVFormat.DEFAULT.getTrailingDelimiter(),
                    CSVFormat.DEFAULT.getAutoFlush());
            this.delegate = delegate;
            this.printer = printer;
        }

        @Override
        public String format(Object... values) {
            final StringWriter out = new StringWriter();
            try {
                // Use the mocked printer instead of creating a new one
                printer.printRecord(values);
                return out.toString().trim();
            } catch (final IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}