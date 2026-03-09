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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Path;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVFormat_32_1Test {

    private CSVFormat csvFormat;
    private Path mockPath;
    private File mockFile;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        mockPath = mock(Path.class);
        mockFile = mock(File.class);
    }

    @Test
    @Timeout(8000)
    public void testPrint_PathAndCharset_Success() throws IOException {
        Charset charset = Charset.defaultCharset();

        when(mockPath.toFile()).thenReturn(mockFile);

        // Spy on csvFormat to mock print(File, Charset)
        CSVFormat spyFormat = Mockito.spy(csvFormat);
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        // Use doReturn().when() without try-catch because doReturn does not throw checked exceptions
        try {
            doReturn(mockPrinter).when(spyFormat).print(eq(mockFile), eq(charset));
        } catch (IOException e) {
            // This block will not be reached because doReturn does not throw checked exceptions
            fail("Unexpected IOException");
        }

        CSVPrinter result = spyFormat.print(mockPath, charset);

        assertNotNull(result);
        assertEquals(mockPrinter, result);

        verify(mockPath, times(1)).toFile();
        try {
            verify(spyFormat, times(1)).print(eq(mockFile), eq(charset));
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    @Timeout(8000)
    public void testPrint_PathNullCharset_ThrowsNullPointerException() throws NoSuchMethodException {
        when(mockPath.toFile()).thenReturn(mockFile);

        Method printMethod = CSVFormat.class.getMethod("print", Path.class, Charset.class);

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            try {
                printMethod.invoke(csvFormat, mockPath, (Charset) null);
            } catch (InvocationTargetException e) {
                // rethrow the underlying exception
                Throwable cause = e.getCause();
                if (cause instanceof NullPointerException) {
                    throw (NullPointerException) cause;
                } else {
                    throw e;
                }
            }
        });

        assertNotNull(thrown);
    }

    @Test
    @Timeout(8000)
    public void testPrint_PathNull_ThrowsNullPointerException() throws NoSuchMethodException {
        Method printMethod = CSVFormat.class.getMethod("print", Path.class, Charset.class);

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            try {
                printMethod.invoke(csvFormat, (Path) null, Charset.defaultCharset());
            } catch (InvocationTargetException e) {
                // rethrow the underlying exception
                Throwable cause = e.getCause();
                if (cause instanceof NullPointerException) {
                    throw (NullPointerException) cause;
                } else {
                    throw e;
                }
            }
        });

        assertNotNull(thrown);
    }

}