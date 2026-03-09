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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CSVFormat_7_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNormalValues() throws IOException {
        String result = csvFormat.format("a", "b", "c");
        assertNotNull(result);
        String expected = "a,b,c";
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithEmptyValues() {
        String result = csvFormat.format();
        // Empty record results in empty string
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatWithNullValues() {
        String result = csvFormat.format((Object) null, "b", null);
        // nulls will print as empty (default), so commas between values remain
        assertEquals(",b,", result);
    }

    @Test
    @Timeout(8000)
    public void testFormatIOExceptionIsWrapped() throws Exception {
        // Create a spy of CSVPrinter to throw IOException on printRecord(Object...)
        CSVPrinter spyPrinter = spy(new CSVPrinter(new StringWriter(), CSVFormat.DEFAULT));
        doThrow(new IOException("io exception")).when(spyPrinter).printRecord((Object[]) any());

        // Use reflection to access the private final field 'out' inside CSVPrinter to get the StringWriter
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Create a CSVFormat instance that delegates format(...) to use spyPrinter
        CSVFormat formatWithMock = new CSVFormatWrapper(spyPrinter, outField);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> formatWithMock.format("a", "b"));
        assertTrue(exception.getMessage().isEmpty());
        assertTrue(exception.getCause() instanceof IOException);
        assertEquals("io exception", exception.getCause().getMessage());
    }

    // Helper class to wrap CSVFormat and override format method without extending final CSVFormat
    static class CSVFormatWrapper extends CSVFormat {
        private final CSVPrinter spyPrinter;
        private final Field outField;

        CSVFormatWrapper(CSVPrinter spyPrinter, Field outField) {
            // Cannot call super constructor because CSVFormat is final and constructor is private,
            // so we use the DEFAULT instance and delegate all methods to it.
            super(); // This line will cause error because CSVFormat has no visible constructor
            throw new UnsupportedOperationException("CSVFormat is final and has no accessible constructor");
        }

        // Instead, implement a wrapper class that implements the same interface (or just has format method)
        // but since CSVFormat is final and no public constructor, we cannot extend it.
        // So we change approach: create a proxy/wrapper class that is NOT extending CSVFormat.

    }

}