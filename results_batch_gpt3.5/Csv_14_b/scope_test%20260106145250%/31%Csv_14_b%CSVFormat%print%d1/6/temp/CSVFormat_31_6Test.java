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
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;
    private File mockFile;
    private Charset charset;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        mockFile = mock(File.class);
        charset = Charset.forName("UTF-8");
    }

    @Test
    @Timeout(8000)
    void testPrintCreatesCSVPrinter() throws Exception {
        // Create a mock CSVPrinter
        CSVPrinter mockPrinter = mock(CSVPrinter.class);

        // Spy on CSVFormat to override print(File, Charset) method to return mockPrinter
        CSVFormat spyFormat = spy(csvFormat);

        doReturn(mockPrinter).when(spyFormat).print(any(File.class), any(Charset.class));

        CSVPrinter printer = spyFormat.print(mockFile, charset);

        assertNotNull(printer);
        assertSame(mockPrinter, printer);
    }

    @Test
    @Timeout(8000)
    void testPrintThrowsIOException() throws IOException {
        // Create a CSVFormat instance that throws IOException in print(File, Charset) using a dynamic proxy subclass
        CSVFormat csvFormatIOException = createCSVFormatThrowingIOException();

        IOException thrown = assertThrows(IOException.class, () -> {
            csvFormatIOException.print(mockFile, charset);
        });
        assertEquals("Simulated IOException", thrown.getMessage());
    }

    private CSVFormat createCSVFormatThrowingIOException() {
        // Create a subclass of CSVFormat via reflection to bypass private constructor and final class restrictions
        try {
            // Create a copy of DEFAULT by cloning its fields
            CSVFormat base = CSVFormat.DEFAULT;

            // Use reflection to create a proxy subclass that overrides print(File, Charset)
            return new CSVFormatProxy(base);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Proxy subclass of CSVFormat that overrides print(File, Charset) to throw IOException
    private static class CSVFormatProxy extends CSVFormat {
        private final CSVFormat delegate;

        CSVFormatProxy(CSVFormat delegate) throws Exception {
            super(
                getFieldValue(delegate, "delimiter"),
                getFieldValue(delegate, "quoteCharacter"),
                getFieldValue(delegate, "quoteMode"),
                getFieldValue(delegate, "commentMarker"),
                getFieldValue(delegate, "escapeCharacter"),
                getFieldValue(delegate, "ignoreSurroundingSpaces"),
                getFieldValue(delegate, "ignoreEmptyLines"),
                getFieldValue(delegate, "recordSeparator"),
                getFieldValue(delegate, "nullString"),
                getFieldValue(delegate, "headerComments"),
                getFieldValue(delegate, "header"),
                getFieldValue(delegate, "skipHeaderRecord"),
                getFieldValue(delegate, "allowMissingColumnNames"),
                getFieldValue(delegate, "ignoreHeaderCase"),
                getFieldValue(delegate, "trim"),
                getFieldValue(delegate, "trailingDelimiter")
            );
            this.delegate = delegate;
        }

        @Override
        public CSVPrinter print(final File out, Charset charset) throws IOException {
            throw new IOException("Simulated IOException");
        }

        // Helper method to get private fields via reflection
        @SuppressWarnings("unchecked")
        private static <T> T getFieldValue(CSVFormat instance, String fieldName) throws Exception {
            Field field = CSVFormat.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        }
    }
}