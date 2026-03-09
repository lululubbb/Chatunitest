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
import java.nio.file.Files;
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

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private Path mockPath;
    private Charset charset;
    private Reader mockedReader;

    @BeforeEach
    void setup() {
        mockPath = mock(Path.class);
        charset = Charset.defaultCharset();
        mockedReader = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    void parse_withValidReader_returnsNonNullParser() throws IOException {
        // Use real CSVFormat to avoid NullPointerException inside CSVParser
        CSVFormat realFormat = CSVFormat.DEFAULT;
        // Because Files.newBufferedReader cannot be mocked, and parse(Path, Charset, CSVFormat) calls it,
        // we test parse(Reader, CSVFormat) directly with a mocked Reader.

        CSVParser parser = CSVParser.parse(mockedReader, realFormat);
        assertNotNull(parser);
    }

    @Test
    @Timeout(8000)
    void parse_withNullPath_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse((Path) null, charset, CSVFormat.DEFAULT));
        assertEquals("path", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_withNullFormat_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> CSVParser.parse(mockPath, charset, null));
        assertEquals("format", ex.getMessage());
    }
}