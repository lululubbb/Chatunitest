package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

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
    void setup() throws IOException {
        mockFile = mock(File.class);
        charset = Charset.defaultCharset();
        mockFormat = mock(CSVFormat.class);

        // Mock file.exists() and file.canRead() to true
        when(mockFile.exists()).thenReturn(true);
        when(mockFile.canRead()).thenReturn(true);
    }

    @Test
    @Timeout(8000)
    void parse_NullFile_ThrowsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((File) null, charset, mockFormat);
        });
        assertEquals("file", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(mockFile, charset, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidFile_ReturnsCSVParser() throws Exception {
        // Create a real temporary file to avoid FileNotFoundException
        File tempFile = File.createTempFile("temp", ".csv");
        tempFile.deleteOnExit();

        // Use a real CSVFormat instance instead of a mock to ensure correct behavior
        CSVFormat realFormat = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(tempFile, charset, realFormat);

        assertNotNull(parser);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(realFormat, formatField.get(parser));
    }
}