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

class CSVParserParseFileTest {

    private File mockFile;
    private Charset charset;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        mockFile = mock(File.class);
        charset = Charset.forName("UTF-8");
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void parse_fileNullFile_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse((File) null, charset, format));
        assertEquals("file", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_fileNullFormat_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse(mockFile, charset, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_fileInvokesInputStreamReaderAndReturnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        java.io.File tempFile = java.io.File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();

        // Use real CSVFormat instead of mock to avoid null pointer issues inside CSVParser
        CSVFormat realFormat = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(tempFile, charset, realFormat);

        assertNotNull(parser);
        // Use reflection to access private field 'format'
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(parser);
        assertEquals(realFormat, actualFormat);
    }
}