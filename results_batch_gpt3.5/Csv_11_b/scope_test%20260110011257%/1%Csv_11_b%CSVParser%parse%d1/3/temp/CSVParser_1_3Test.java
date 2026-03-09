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
import java.nio.charset.Charset;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private File fileMock;
    private Charset charset;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        fileMock = mock(File.class);
        charset = Charset.defaultCharset();
        formatMock = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void parse_NullFile_ThrowsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((File) null, charset, formatMock);
        });
        assertEquals("file", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(fileMock, charset, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidFile_ReturnsCSVParser() throws IOException {
        // Stub methods used internally by CSVParser to avoid IOException
        when(fileMock.exists()).thenReturn(true);
        when(fileMock.isFile()).thenReturn(true);
        when(fileMock.canRead()).thenReturn(true);
        when(fileMock.length()).thenReturn(0L);
        when(fileMock.getPath()).thenReturn("dummy.csv");

        CSVParser parser = CSVParser.parse(fileMock, charset, formatMock);
        assertNotNull(parser);
        assertEquals(formatMock, getField(parser, "format"));
    }

    // Helper method to access private fields using reflection
    @SuppressWarnings("unchecked")
    private <T> T getField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}