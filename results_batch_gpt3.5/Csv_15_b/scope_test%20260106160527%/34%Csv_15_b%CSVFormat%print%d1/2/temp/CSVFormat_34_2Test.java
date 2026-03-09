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
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testPrint_withValidPathAndCharset() throws IOException {
        Path mockPath = mock(Path.class);
        CSVPrinter mockPrinter = mock(CSVPrinter.class);

        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            // Use a real Writer or mock Writer since print expects BufferedWriter (Writer)
            Writer mockWriter = mock(Writer.class);
            mockedFiles.when(() -> Files.newBufferedWriter(mockPath, StandardCharsets.UTF_8))
                    .thenReturn(mockWriter);

            CSVFormat spyFormat = spy(csvFormat);
            doReturn(mockPrinter).when(spyFormat).print(any(Appendable.class));

            CSVPrinter printer = spyFormat.print(mockPath, StandardCharsets.UTF_8);
            assertNotNull(printer);
            verify(spyFormat, times(1)).print(any(Appendable.class));
        }
    }

    @Test
    @Timeout(8000)
    public void testPrint_withNullPath_throwsNullPointerException() {
        // Explicitly cast null to Path to avoid ambiguity between print(File, Charset) and print(Path, Charset)
        assertThrows(NullPointerException.class, () -> csvFormat.print((Path) null, StandardCharsets.UTF_8));
    }

    @Test
    @Timeout(8000)
    public void testPrint_withNullCharset_throwsNullPointerException() {
        Path mockPath = mock(Path.class);
        assertThrows(NullPointerException.class, () -> csvFormat.print(mockPath, (Charset) null));
    }

    @Test
    @Timeout(8000)
    public void testPrint_invokesPrivatePrintMethod() throws Exception {
        Appendable mockAppendable = mock(Appendable.class);
        CSVFormat spyFormat = spy(csvFormat);

        Method method = CSVFormat.class.getDeclaredMethod("print", Appendable.class);
        method.setAccessible(true);

        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        doReturn(mockPrinter).when(spyFormat).print(any(Appendable.class));

        CSVPrinter result = (CSVPrinter) method.invoke(spyFormat, mockAppendable);
        assertSame(mockPrinter, result);
    }
}