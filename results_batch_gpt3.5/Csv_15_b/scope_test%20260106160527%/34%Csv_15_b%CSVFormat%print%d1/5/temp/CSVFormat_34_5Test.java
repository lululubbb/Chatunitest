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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Method;
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
    public void setup() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testPrintWithPathAndCharset() throws IOException {
        Path mockPath = mock(Path.class);
        BufferedWriter mockWriter = mock(BufferedWriter.class);
        CSVPrinter mockPrinter = mock(CSVPrinter.class);

        MockedStatic<Files> filesStatic = null;
        try {
            Method mockStaticMethod = Mockito.class.getMethod("mockStatic", Class.class);
            filesStatic = (MockedStatic<Files>) mockStaticMethod.invoke(null, Files.class);
        } catch (Exception e) {
            fail("Failed to create MockedStatic for Files: " + e.getMessage());
        }

        try (MockedStatic<Files> ignored = filesStatic) {
            filesStatic.when(() -> Files.newBufferedWriter(mockPath, StandardCharsets.UTF_8)).thenReturn(mockWriter);

            CSVFormat spyFormat = spy(csvFormat);
            doReturn(mockPrinter).when(spyFormat).print(mockWriter);

            CSVPrinter printer = spyFormat.print(mockPath, StandardCharsets.UTF_8);

            assertNotNull(printer);
            assertEquals(mockPrinter, printer);

            filesStatic.verify(() -> Files.newBufferedWriter(mockPath, StandardCharsets.UTF_8));
            verify(spyFormat).print(mockWriter);
        } finally {
            if (filesStatic != null) {
                filesStatic.close();
            }
        }
    }

    @Test
    @Timeout(8000)
    public void testPrintWithNullPathThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            csvFormat.print((Path) null, StandardCharsets.UTF_8);
        });
    }

    @Test
    @Timeout(8000)
    public void testPrintWithNullCharsetThrowsException() {
        Path mockPath = mock(Path.class);
        assertThrows(NullPointerException.class, () -> {
            csvFormat.print(mockPath, null);
        });
    }

    @Test
    @Timeout(8000)
    public void testPrintWithRealPathAndCharset() throws IOException {
        Path tempFile = Files.createTempFile("csvformattest", ".csv");
        tempFile.toFile().deleteOnExit();

        CSVFormat format = CSVFormat.DEFAULT;

        CSVPrinter printer = format.print(tempFile, StandardCharsets.UTF_8);
        assertNotNull(printer);

        printer.close();
    }
}