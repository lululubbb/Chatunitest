package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_4_4Test {

    private Reader mockReader;
    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        mockFormat = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_NullReader_ThrowsException() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new CSVParser(null, mockFormat));
        assertEquals("reader", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_NullFormat_ThrowsException() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new CSVParser(mockReader, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_InitializesFields() throws Exception {
        // Mock CSVFormat behavior if needed
        when(mockFormat.getHeader()).thenReturn(null);
        when(mockFormat.getSkipHeaderRecord()).thenReturn(false);

        // Since Lexer and ExtendedBufferedReader are internal classes,
        // and CSVParser constructor calls initializeHeader which may use them,
        // we need to mock or spy CSVFormat sufficiently for constructor to succeed.

        CSVParser parser = new CSVParser(mockReader, mockFormat);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(mockFormat, formatField.get(parser));

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        Object lexer = lexerField.get(parser);
        assertNotNull(lexer);

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        Map<?, ?> headerMap = (Map<?, ?>) headerMapField.get(parser);
        assertNotNull(headerMap);
    }
}