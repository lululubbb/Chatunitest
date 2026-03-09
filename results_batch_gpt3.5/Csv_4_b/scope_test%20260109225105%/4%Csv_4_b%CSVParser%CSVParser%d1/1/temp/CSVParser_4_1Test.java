package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_4_1Test {

    private CSVFormat formatMock;
    private Reader reader;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        reader = new StringReader("header1,header2\nvalue1,value2");
    }

    @Test
    @Timeout(8000)
    void testConstructor_NullReader_ThrowsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new CSVParser(null, formatMock);
        });
        assertEquals("reader", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_NullFormat_ThrowsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new CSVParser(reader, null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_FormatValidateCalledAndFieldsSet() throws Exception {
        doNothing().when(formatMock).validate();

        CSVParser parser = new CSVParser(reader, formatMock);

        verify(formatMock, times(1)).validate();

        // Using reflection to check private fields
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(formatMock, formatField.get(parser));

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        assertNotNull(lexerField.get(parser));

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        Map<?, ?> headerMap = (Map<?, ?>) headerMapField.get(parser);
        assertNotNull(headerMap);
    }

    @Test
    @Timeout(8000)
    void testConstructor_FormatValidateThrowsIOException() throws IOException {
        doThrow(new IOException("validate failed")).when(formatMock).validate();

        IOException ex = assertThrows(IOException.class, () -> {
            new CSVParser(reader, formatMock);
        });
        assertEquals("validate failed", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testInitializeHeader_PrivateMethod() throws Exception {
        doNothing().when(formatMock).validate();

        CSVParser parser = new CSVParser(reader, formatMock);

        Method initHeader = CSVParser.class.getDeclaredMethod("initializeHeader");
        initHeader.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) initHeader.invoke(parser);
        assertNotNull(headerMap);
    }
}