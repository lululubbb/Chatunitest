package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class CSVParserParsePathTest {

    private Path mockPath;
    private Charset charset;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        mockPath = mock(Path.class);
        charset = Charset.defaultCharset();
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void parse_nullPath_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse((Path) null, charset, format));
        assertEquals("path", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_nullFormat_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse(mockPath, charset, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_returnsNonNullParser() throws IOException {
        MockedStatic<Files> mockedFiles = null;
        MockedStatic<CSVParser> mockedCSVParser = null;
        try {
            mockedFiles = mockStatic(Files.class);
            BufferedReader mockReader = mock(BufferedReader.class);
            mockedFiles.when(() -> Files.newBufferedReader(mockPath, charset)).thenReturn(mockReader);

            mockedCSVParser = mockStatic(CSVParser.class);
            mockedCSVParser.when(() -> CSVParser.parse(mockReader, format)).thenReturn(new CSVParser(mockReader, format));

            CSVParser actualParser = CSVParser.parse(mockPath, charset, format);
            assertNotNull(actualParser);
        } finally {
            if (mockedCSVParser != null) {
                mockedCSVParser.close();
            }
            if (mockedFiles != null) {
                mockedFiles.close();
            }
        }
    }
}