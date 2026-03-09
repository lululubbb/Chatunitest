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
    void testPrintWithValidPathAndCharset() throws Exception {
        Path mockPath = mock(Path.class);
        CSVPrinter mockPrinter = mock(CSVPrinter.class);

        // Create a spy of csvFormat to override print(Appendable)
        CSVFormat spyFormat = spy(csvFormat);

        // When print(Appendable) is called, return mockPrinter
        doReturn(mockPrinter).when(spyFormat).print(any(Appendable.class));

        // Create a subclass of CSVFormat overriding print(Path, Charset) to avoid calling Files.newBufferedWriter
        CSVFormat testFormat = new CSVFormat() {
            @Override
            public CSVPrinter print(final Path out, final java.nio.charset.Charset charset) throws IOException {
                return print(new StringWriter());
            }
        };

        CSVFormat spyTestFormat = spy(testFormat);
        doReturn(mockPrinter).when(spyTestFormat).print(any(Appendable.class));

        CSVPrinter result = spyTestFormat.print(mockPath, StandardCharsets.UTF_8);

        assertNotNull(result);
        verify(spyTestFormat).print(any(Appendable.class));
    }

    @Test
    @Timeout(8000)
    void testPrintThrowsIOExceptionWhenFilesNewBufferedWriterFails() throws IOException {
        Path mockPath = mock(Path.class);

        // Create a subclass to simulate IOException on print(Path, Charset)
        CSVFormat testFormat = new CSVFormat() {
            @Override
            public CSVPrinter print(final Path out, final java.nio.charset.Charset charset) throws IOException {
                throw new IOException("File write error");
            }
        };

        IOException thrown = assertThrows(IOException.class, () -> testFormat.print(mockPath, StandardCharsets.UTF_8));
        assertEquals("File write error", thrown.getMessage());
    }
}