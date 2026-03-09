package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_4_6Test {

    private CSVFormat mockFormat;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockFormat = mock(CSVFormat.class);
        mockReader = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_NullReader_ThrowsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new CSVParser(null, mockFormat);
        });
        assertEquals("reader", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_NullFormat_ThrowsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new CSVParser(mockReader, null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_ValidParameters_InitializesFields() throws Exception {
        doNothing().when(mockFormat).validate();

        CSVParser parser = new CSVParser(mockReader, mockFormat);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(mockFormat, formatField.get(parser));

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        assertNotNull(lexerField.get(parser));

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) headerMapField.get(parser);
        assertNotNull(headerMap);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_FormatValidateCalled() throws IOException {
        CSVFormat spyFormat = spy(CSVFormat.class);
        doNothing().when(spyFormat).validate();

        new CSVParser(mockReader, spyFormat);

        verify(spyFormat, times(1)).validate();
    }

    @Test
    @Timeout(8000)
    public void testInitializeHeader_PrivateMethod() throws Exception {
        doNothing().when(mockFormat).validate();
        CSVParser parser = new CSVParser(mockReader, mockFormat);

        Method initializeHeader = CSVParser.class.getDeclaredMethod("initializeHeader");
        initializeHeader.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) initializeHeader.invoke(parser);
        assertNotNull(headerMap);
    }
}