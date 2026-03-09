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

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    @BeforeEach
    void setUp() {
        // No static mocking because Mockito 3 does not support mockStatic
    }

    @AfterEach
    void tearDown() {
        // No static mocking to close
    }

    @Test
    @Timeout(8000)
    void parse_givenNullPath_throwsException() {
        Path path = null;
        Charset charset = Charset.defaultCharset();
        CSVFormat format = mock(CSVFormat.class);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(path, charset, format);
        });
        assertEquals("path", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_givenNullFormat_throwsException() {
        Path path = mock(Path.class);
        Charset charset = Charset.defaultCharset();
        CSVFormat format = null;

        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(path, charset, format);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_readsBufferedReaderAndDelegates() throws Exception {
        BufferedReader reader = mock(BufferedReader.class);
        CSVFormat format = mock(CSVFormat.class);

        Method parseMethod = CSVParser.class.getMethod("parse", java.io.Reader.class, CSVFormat.class);

        CSVParser result = (CSVParser) parseMethod.invoke(null, reader, format);

        assertNotNull(result);
    }
}