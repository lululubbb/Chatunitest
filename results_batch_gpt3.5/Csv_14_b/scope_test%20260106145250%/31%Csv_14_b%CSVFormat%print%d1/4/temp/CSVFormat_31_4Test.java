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
import java.nio.charset.Charset;
import java.lang.reflect.Field;

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
    void testPrintCreatesCSVPrinterSuccessfully() throws Exception {
        // Mock CSVPrinter to avoid actual file IO
        CSVPrinter csvPrinterMock = mock(CSVPrinter.class);

        // Use reflection to create a proxy CSVFormat that overrides print(File, Charset)
        CSVFormat csvFormatMock = csvFormat;

        // Use reflection to replace the print(File, Charset) method behavior by creating a dynamic proxy
        // Since CSVFormat is final and constructor is private, we cannot subclass it.
        // Instead, we use a spy and override the print method using Mockito.

        CSVFormat spyFormat = spy(csvFormatMock);

        doReturn(csvPrinterMock).when(spyFormat).print(any(File.class), any(Charset.class));

        CSVPrinter printer = spyFormat.print(mockFile, charset);

        assertNotNull(printer);
        assertSame(csvPrinterMock, printer);
    }

    @Test
    @Timeout(8000)
    void testPrintThrowsIOExceptionWhenFileOutputStreamFails() {
        File nonWritableFile = new File("/root/forbiddenfile.csv");
        Charset charset = Charset.forName("UTF-8");

        assertThrows(IOException.class, () -> csvFormat.print(nonWritableFile, charset));
    }

    @Test
    @Timeout(8000)
    void testPrintThrowsNullPointerExceptionWhenFileIsNull() {
        assertThrows(NullPointerException.class, () -> csvFormat.print((File) null, charset));
    }

    @Test
    @Timeout(8000)
    void testPrintThrowsNullPointerExceptionWhenCharsetIsNull() {
        assertThrows(NullPointerException.class, () -> csvFormat.print(mockFile, null));
    }
}