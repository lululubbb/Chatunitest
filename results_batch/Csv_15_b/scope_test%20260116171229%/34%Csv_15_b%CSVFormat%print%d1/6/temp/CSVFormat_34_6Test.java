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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.io.BufferedWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_WithValidPathAndCharset_ReturnsCSVPrinter() throws IOException {
        Path mockPath = mock(Path.class);
        BufferedWriter mockWriter = mock(BufferedWriter.class);

        MockedStatic<Files> filesMockedStatic = null;
        try {
            // Use reflection to get mockStatic method and invoke it
            Object mockedStaticObj = Mockito.class
                    .getMethod("mockStatic", Class.class)
                    .invoke(null, Files.class);
            filesMockedStatic = (MockedStatic<Files>) mockedStaticObj;

            filesMockedStatic.when(() -> Files.newBufferedWriter(mockPath, StandardCharsets.UTF_8))
                    .thenReturn(mockWriter);

            CSVPrinter printer = csvFormat.print(mockPath, StandardCharsets.UTF_8);

            assertNotNull(printer);
            filesMockedStatic.verify(() -> Files.newBufferedWriter(mockPath, StandardCharsets.UTF_8));
        } catch (ReflectiveOperationException e) {
            fail("Reflection failed to mock static method: " + e.getMessage());
        } finally {
            if (filesMockedStatic != null) {
                filesMockedStatic.close();
            }
        }
    }

    @Test
    @Timeout(8000)
    void testPrint_IOExceptionThrown_PropagatesException() throws IOException {
        Path mockPath = mock(Path.class);

        MockedStatic<Files> filesMockedStatic = null;
        try {
            Object mockedStaticObj = Mockito.class
                    .getMethod("mockStatic", Class.class)
                    .invoke(null, Files.class);
            filesMockedStatic = (MockedStatic<Files>) mockedStaticObj;

            filesMockedStatic.when(() -> Files.newBufferedWriter(mockPath, StandardCharsets.UTF_8))
                    .thenThrow(new IOException("Mock IO Exception"));

            IOException thrown = assertThrows(IOException.class, () -> {
                csvFormat.print(mockPath, StandardCharsets.UTF_8);
            });
            assertEquals("Mock IO Exception", thrown.getMessage());
        } catch (ReflectiveOperationException e) {
            fail("Reflection failed to mock static method: " + e.getMessage());
        } finally {
            if (filesMockedStatic != null) {
                filesMockedStatic.close();
            }
        }
    }

    @Test
    @Timeout(8000)
    void testPrint_RealBufferedWriter() throws IOException {
        Path tempFile = Files.createTempFile("csvformattest", ".csv");
        tempFile.toFile().deleteOnExit();

        CSVPrinter printer = csvFormat.print(tempFile, StandardCharsets.UTF_8);
        assertNotNull(printer);

        printer.printRecord("a", "b", "c");
        printer.close();
    }
}