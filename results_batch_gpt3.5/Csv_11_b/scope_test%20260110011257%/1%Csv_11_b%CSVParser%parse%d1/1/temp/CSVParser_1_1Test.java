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

class CSVParser_1_1Test {

    private Charset charset;
    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() {
        charset = Charset.defaultCharset();
        mockFormat = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testParseWithValidFileAndCharsetAndFormat() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Create a temporary file for testing
        File tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();

        CSVParser parser = CSVParser.parse(tempFile, charset, mockFormat);
        assertNotNull(parser);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object formatValue = formatField.get(parser);
        assertEquals(mockFormat, formatValue);
    }

    @Test
    @Timeout(8000)
    void testParseWithNullFileThrowsException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((File) null, charset, mockFormat);
        });
        assertEquals("file", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParseWithNullFormatThrowsException() {
        final File tempFile;
        try {
            tempFile = File.createTempFile("test", ".csv");
            tempFile.deleteOnExit();
        } catch (IOException e) {
            fail("Failed to create temp file for testing");
            return; // to satisfy compiler about tempFile initialization
        }

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(tempFile, charset, null);
        });
        assertEquals("format", thrown.getMessage());
    }
}