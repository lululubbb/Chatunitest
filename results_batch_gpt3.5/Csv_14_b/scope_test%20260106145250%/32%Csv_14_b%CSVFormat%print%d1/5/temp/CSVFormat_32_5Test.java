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
import java.nio.charset.Charset;
import java.nio.file.Path;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
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
    void testPrint_withPath_callsPrintFile() throws Exception {
        Path mockPath = mock(Path.class);
        File mockFile = mock(File.class);
        Charset charset = Charset.defaultCharset();

        when(mockPath.toFile()).thenReturn(mockFile);

        CSVFormat spyFormat = spy(csvFormat);
        CSVPrinter expectedPrinter = mock(CSVPrinter.class);

        // Use doReturn().when() instead of when().thenReturn() for spying final methods
        doReturn(expectedPrinter).when(spyFormat).print(eq(mockFile), eq(charset));

        CSVPrinter actualPrinter = spyFormat.print(mockPath, charset);

        verify(mockPath).toFile();
        verify(spyFormat).print(mockFile, charset);
        assertSame(expectedPrinter, actualPrinter);
    }

    @Test
    @Timeout(8000)
    void testPrintFile_writesToFile() throws IOException {
        File tempFile = File.createTempFile("csvformattest", ".csv");
        tempFile.deleteOnExit();
        Charset charset = Charset.defaultCharset();

        CSVPrinter printer = csvFormat.print(tempFile, charset);

        assertNotNull(printer);
        printer.close();
    }

    @Test
    @Timeout(8000)
    void testPrintFile_throwsIOException() throws Exception {
        File nonWritableFile = mock(File.class);
        Charset charset = Charset.defaultCharset();

        CSVFormat spyFormat = spy(csvFormat);

        doThrow(new IOException("Simulated IOException"))
                .when(spyFormat).print(eq(nonWritableFile), eq(charset));

        IOException thrown = assertThrows(IOException.class, () -> {
            spyFormat.print(nonWritableFile, charset);
        });

        assertEquals("Simulated IOException", thrown.getMessage());
    }
}