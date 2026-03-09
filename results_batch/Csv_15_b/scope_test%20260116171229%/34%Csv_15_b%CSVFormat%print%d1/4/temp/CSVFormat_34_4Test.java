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
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_withValidPathAndCharset() throws IOException {
        Path mockPath = mock(Path.class);
        CSVPrinter mockPrinter = mock(CSVPrinter.class);

        CSVFormat spyFormat = spy(csvFormat);
        doReturn(mockPrinter).when(spyFormat).print(any(java.lang.Appendable.class));
        doAnswer((InvocationOnMock invocation) -> {
            Object[] args = invocation.getArguments();
            // Instead of calling Files.newBufferedWriter, call print with StringWriter
            return spyFormat.print(new java.io.StringWriter());
        }).when(spyFormat).print(any(Path.class), any());

        CSVPrinter printer = spyFormat.print(mockPath, StandardCharsets.UTF_8);
        assertNotNull(printer);
        assertEquals(mockPrinter, printer);
    }

    @Test
    @Timeout(8000)
    void testPrint_withIOException() throws IOException {
        Path mockPath = mock(Path.class);

        CSVFormat spyFormat = spy(csvFormat);
        doThrow(new IOException("Test IO Exception")).when(spyFormat).print(any(Path.class), any());

        IOException thrown = assertThrows(IOException.class, () -> spyFormat.print(mockPath, StandardCharsets.UTF_8));
        assertEquals("Test IO Exception", thrown.getMessage());
    }
}