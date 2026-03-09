package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private Charset charset;
    private CSVFormat formatMock;

    @BeforeEach
    void setup() {
        charset = Charset.defaultCharset();
        formatMock = mock(CSVFormat.class);
        // Mock equals method to avoid false negatives in assertEquals
        when(formatMock.equals(any())).thenAnswer(invocation -> invocation.getArgument(0) == formatMock);
    }

    @Test
    @Timeout(8000)
    void parse_withValidFile_shouldReturnCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("test", ".csv");
        java.nio.file.Files.write(tempFile, "header1,header2\nvalue1,value2".getBytes(charset));

        CSVParser parser = CSVParser.parse(tempFile.toFile(), charset, formatMock);

        assertNotNull(parser);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(parser);

        assertSame(formatMock, actualFormat);

        java.nio.file.Files.deleteIfExists(tempFile);
    }

    @Test
    @Timeout(8000)
    void parse_nullFile_shouldThrowNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((File) null, charset, formatMock);
        });
        assertEquals("file", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_nullFormat_shouldThrowNullPointerException() {
        try {
            java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("test", ".csv");
            java.nio.file.Files.write(tempFile, "header1,header2\nvalue1,value2".getBytes(charset));

            NullPointerException ex = assertThrows(NullPointerException.class, () -> {
                CSVParser.parse(tempFile.toFile(), charset, null);
            });
            assertEquals("format", ex.getMessage());

            java.nio.file.Files.deleteIfExists(tempFile);
        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }
}