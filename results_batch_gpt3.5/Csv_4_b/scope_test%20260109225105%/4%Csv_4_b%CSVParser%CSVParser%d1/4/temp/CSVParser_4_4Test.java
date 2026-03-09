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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_4_4Test {

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
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            new CSVParser(null, mockFormat);
        });
        assertEquals("reader", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_NullFormat_ThrowsException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            new CSVParser(mockReader, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_ValidArguments_InitializesFields() throws Exception {
        // Mock format.validate() to do nothing
        doNothing().when(mockFormat).validate();

        // Use reflection to invoke constructor
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        CSVParser parser = constructor.newInstance(mockReader, mockFormat);

        // Verify format.validate() called
        verify(mockFormat).validate();

        // Verify fields set
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