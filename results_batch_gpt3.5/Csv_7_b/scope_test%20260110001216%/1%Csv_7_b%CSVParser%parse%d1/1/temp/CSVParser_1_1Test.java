package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_1_1Test {

    private File mockFile;
    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() {
        mockFile = mock(File.class);
        mockFormat = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testParse_withValidFileAndFormat_returnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Use a real temporary file instead of mockFile
        java.io.File tempFile = java.io.File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();

        CSVParser parser = CSVParser.parse(tempFile, mockFormat);
        assertNotNull(parser);

        // Access private final field 'format' via reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(parser);
        assertSame(mockFormat, actualFormat);
    }

    @Test
    @Timeout(8000)
    void testParse_withNullFile_throwsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> CSVParser.parse((File) null, mockFormat));
        assertEquals("file", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullFormat_throwsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> CSVParser.parse(mockFile, null));
        assertEquals("format", thrown.getMessage());
    }
}