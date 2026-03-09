package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class CSVParserParsePathTest {

    private Path mockPath;
    private Charset charset;
    private CSVFormat format;
    private Reader mockReader;

    @BeforeEach
    void setUp() {
        mockPath = mock(Path.class);
        charset = Charset.defaultCharset();
        format = mock(CSVFormat.class);
        mockReader = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    void parse_nullPath_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((Path) null, charset, format);
        });
        assertEquals("path", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_nullFormat_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockPath, charset, null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_callsFilesNewBufferedReader_andDelegates() throws IOException {
        try (MockedStatic<org.apache.commons.csv.Assertions> mockedAssertions = Mockito.mockStatic(org.apache.commons.csv.Assertions.class);
             MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class);
             MockedStatic<CSVParser> mockedCSVParser = Mockito.mockStatic(CSVParser.class)) {

            // Assertions.notNull does nothing (no exception)
            mockedAssertions.when(() -> org.apache.commons.csv.Assertions.notNull(any(), any())).thenAnswer(invocation -> null);

            // Files.newBufferedReader returns mockReader wrapped in BufferedReader
            mockedFiles.when(() -> Files.newBufferedReader(mockPath, charset)).thenReturn(new BufferedReader(mockReader));

            CSVParser expectedParser = mock(CSVParser.class);

            // CSVParser.parse(Reader, CSVFormat) returns expectedParser
            mockedCSVParser.when(() -> CSVParser.parse(any(Reader.class), eq(format))).thenReturn(expectedParser);

            // To avoid recursion, mock CSVParser.parse(Path, Charset, CSVFormat) to call real method
            mockedCSVParser.when(() -> CSVParser.parse(mockPath, charset, format)).thenCallRealMethod();

            CSVParser result = CSVParser.parse(mockPath, charset, format);

            assertSame(expectedParser, result);

            // Verify static calls
            mockedAssertions.verify(() -> org.apache.commons.csv.Assertions.notNull(mockPath, "path"));
            mockedAssertions.verify(() -> org.apache.commons.csv.Assertions.notNull(format, "format"));
            mockedFiles.verify(() -> Files.newBufferedReader(mockPath, charset));
            mockedCSVParser.verify(() -> CSVParser.parse(any(Reader.class), eq(format)));
        }
    }
}