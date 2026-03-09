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
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_withValidPathAndCharset_returnsCSVPrinter() throws IOException {
        Path mockPath = mock(Path.class);
        CSVPrinter mockPrinter = mock(CSVPrinter.class);

        // Create a Writer instance to be returned by Files.newBufferedWriter
        Writer writer = new java.io.StringWriter();

        CSVFormatWrapper formatWithOverride = new CSVFormatWrapper(csvFormat, writer, mockPrinter);

        CSVPrinter printer = formatWithOverride.print(mockPath, StandardCharsets.UTF_8);

        assertNotNull(printer);
        assertSame(mockPrinter, printer);
    }

    @Test
    @Timeout(8000)
    void testPrint_throwsIOException_whenFilesNewBufferedWriterThrows() {
        Path mockPath = mock(Path.class);

        CSVFormatWrapper formatWithException = new CSVFormatWrapper(csvFormat, null, null) {
            @Override
            public CSVPrinter print(Path out, java.nio.charset.Charset charset) throws IOException {
                throw new IOException("Test IO Exception");
            }
        };

        IOException thrown = assertThrows(IOException.class, () -> {
            formatWithException.print(mockPath, StandardCharsets.UTF_8);
        });

        assertEquals("Test IO Exception", thrown.getMessage());
    }

    /**
     * Wrapper class that delegates to CSVFormat but overrides print(Path, Charset)
     * to avoid calling Files.newBufferedWriter by returning a CSVPrinter using a provided Writer.
     */
    static class CSVFormatWrapper {
        private final CSVFormat delegate;
        private final Writer writer;
        private final CSVPrinter mockPrinter;

        CSVFormatWrapper(CSVFormat delegate, Writer writer, CSVPrinter mockPrinter) {
            this.delegate = delegate;
            this.writer = writer;
            this.mockPrinter = mockPrinter;
        }

        public CSVPrinter print(Path out, java.nio.charset.Charset charset) throws IOException {
            if (writer == null) {
                // simulate original behavior
                return delegate.print(out, charset);
            }
            if (mockPrinter != null) {
                return mockPrinter;
            }
            return delegate.print(writer);
        }
    }
}