package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
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

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVParserIsClosedTest {

    private CSVParser parser;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        // Create a real CSVParser instance using a dummy Reader and null format
        parser = new CSVParser(Mockito.mock(Reader.class), null);

        // Use reflection to inject a mocked Lexer into the private final field 'lexer'
        lexerMock = mock(Lexer.class);
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from the 'lexer' field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        // Set the mocked lexer
        lexerField.set(parser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void testIsClosedReturnsTrue() {
        when(lexerMock.isClosed()).thenReturn(true);

        boolean result = parser.isClosed();

        assertTrue(result);
        verify(lexerMock).isClosed();
    }

    @Test
    @Timeout(8000)
    void testIsClosedReturnsFalse() {
        when(lexerMock.isClosed()).thenReturn(false);

        boolean result = parser.isClosed();

        assertFalse(result);
        verify(lexerMock).isClosed();
    }
}