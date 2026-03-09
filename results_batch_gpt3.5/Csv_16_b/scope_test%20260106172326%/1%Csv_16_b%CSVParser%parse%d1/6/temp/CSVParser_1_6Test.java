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
import java.lang.reflect.Field;
import java.nio.charset.Charset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private File mockFile;
    private Charset charset;
    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() throws IOException {
        mockFile = mock(File.class);
        charset = Charset.defaultCharset();
        mockFormat = mock(CSVFormat.class);

        when(mockFile.exists()).thenReturn(true);
        when(mockFile.canRead()).thenReturn(true);
    }

    @Test
    @Timeout(8000)
    void parse_withNullFile_throwsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((File) null, charset, mockFormat);
        });
        assertEquals("file", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_withNullFormat_throwsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockFile, charset, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_withValidArguments_returnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        java.io.File tempFile = java.io.File.createTempFile("csvparser", ".csv");
        tempFile.deleteOnExit();

        CSVParser parser = CSVParser.parse(tempFile, charset, mockFormat);
        assertNotNull(parser);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(parser);

        assertEquals(mockFormat, actualFormat);
    }
}