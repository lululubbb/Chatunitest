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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    void testPrint_WithValidPathAndCharset_ReturnsCSVPrinter() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Path mockPath = mock(Path.class);
        CSVPrinter mockPrinter = mock(CSVPrinter.class);

        // Spy on csvFormat to mock print(Appendable) method
        CSVFormat spyFormat = spy(csvFormat);

        // Mock print(Appendable) to return mockPrinter
        try {
            doReturn(mockPrinter).when(spyFormat).print(any(Appendable.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Mock Files.newBufferedWriter(Path, Charset) indirectly by mocking print(Path, Charset) method to call print(Appendable)
        // Instead of invoking the real print(Path, Charset), we invoke the private method print(Appendable) directly via spyFormat
        // But since we want to test print(Path, Charset), use reflection to call print(Path, Charset) on spyFormat with a mock Path,
        // and mock print(Appendable) to avoid real file IO.

        // To avoid the actual Files.newBufferedWriter call inside print(Path, Charset),
        // we can mock the static Files.newBufferedWriter method using Mockito's inline mock maker or PowerMockito,
        // but since only Mockito 3 is allowed (no PowerMockito), we rely on mocking print(Appendable) only.

        // However, print(Path, Charset) calls Files.newBufferedWriter(out, charset) internally,
        // so to avoid IOException due to mockPath, we can mock the Path to return a real temporary file path,
        // or better, mock the Files.newBufferedWriter method using reflection to replace it temporarily.

        // Since mocking static methods is not allowed here, instead, invoke print(Appendable) directly.

        // Use reflection to get print(Path, Charset) method
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Path.class, java.nio.charset.Charset.class);
        printMethod.setAccessible(true);

        // Because print(Path, Charset) calls Files.newBufferedWriter, which will fail with mock Path,
        // we need to mock Files.newBufferedWriter or provide a real Path.

        // Workaround: create a temporary file Path to avoid IOException
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("csvtest", ".csv");
        tempFile.toFile().deleteOnExit();

        // Spy on spyFormat.print(Appendable) to return mockPrinter
        CSVPrinter result = (CSVPrinter) printMethod.invoke(spyFormat, tempFile, StandardCharsets.UTF_8);

        assertNotNull(result);
        assertEquals(mockPrinter, result);

        verify(spyFormat).print(any(Appendable.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_ThrowsIOExceptionWhenPrintAppendableFails() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        // Create a temporary file Path to avoid IOException in Files.newBufferedWriter
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("csvtest", ".csv");
        tempFile.toFile().deleteOnExit();

        // Spy on csvFormat to mock print(Appendable) method to throw IOException
        CSVFormat spyFormat = spy(csvFormat);

        try {
            doAnswer(invocation -> {
                throw new IOException("Test IO Exception");
            }).when(spyFormat).print(any(Appendable.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Path.class, java.nio.charset.Charset.class);
        printMethod.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            printMethod.invoke(spyFormat, tempFile, StandardCharsets.UTF_8);
        });

        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertEquals(IOException.class, cause.getClass());
        assertEquals("Test IO Exception", cause.getMessage());
    }
}