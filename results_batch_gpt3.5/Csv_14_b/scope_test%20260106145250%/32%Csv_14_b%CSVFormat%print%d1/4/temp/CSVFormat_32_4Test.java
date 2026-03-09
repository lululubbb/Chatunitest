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
import org.mockito.Mockito;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testPrint_withPath_callsPrintWithFile() throws Exception {
        // Arrange
        Path mockPath = mock(Path.class);
        File mockFile = mock(File.class);
        Charset charset = Charset.forName("UTF-8");
        CSVPrinter mockPrinter = mock(CSVPrinter.class);

        when(mockPath.toFile()).thenReturn(mockFile);

        // Spy on csvFormat to mock print(File, Charset)
        CSVFormat spyFormat = Mockito.spy(csvFormat);

        // Use reflection to suppress the final modifier by using doReturn on spy directly
        doReturn(mockPrinter).when(spyFormat).print(any(File.class), eq(charset));

        // Act
        CSVPrinter printer = spyFormat.print(mockPath, charset);

        // Assert
        assertNotNull(printer);
        assertSame(mockPrinter, printer);
        verify(mockPath).toFile();
        verify(spyFormat).print(mockFile, charset);
    }

    @Test
    @Timeout(8000)
    public void testPrint_withNullCharset_throwsNullPointerException() throws Exception {
        Path mockPath = mock(Path.class);

        assertThrows(NullPointerException.class, () -> csvFormat.print(mockPath, (Charset) null));
    }

    @Test
    @Timeout(8000)
    public void testPrint_withNullPath_throwsNullPointerException() throws Exception {
        Charset charset = Charset.forName("UTF-8");

        assertThrows(NullPointerException.class, () -> csvFormat.print((Path) null, charset));
    }

    @Test
    @Timeout(8000)
    public void testPrint_withRealPathAndCharset_returnsPrinter() throws IOException {
        Path tempPath = Path.of(System.getProperty("java.io.tmpdir"), "csvformattest.csv");
        Charset charset = Charset.forName("UTF-8");

        CSVFormat format = CSVFormat.DEFAULT;

        CSVPrinter printer = format.print(tempPath, charset);

        assertNotNull(printer);
        printer.close();
        // Cleanup file if created
        File file = tempPath.toFile();
        if (file.exists()) {
            file.delete();
        }
    }
}