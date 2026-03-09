package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_12_1Test {

    private CSVParser csvParser;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        // Create a CSVParser instance with a dummy Reader and CSVFormat
        csvParser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Use reflection to inject a mocked Lexer into csvParser
        lexerMock = mock(Lexer.class);
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier if present (Java 12+ may prevent this; assume Java 8+)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(csvParser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void testIsClosed_whenLexerIsClosed_returnsTrue() {
        when(lexerMock.isClosed()).thenReturn(true);
        assertTrue(csvParser.isClosed());
        verify(lexerMock).isClosed();
    }

    @Test
    @Timeout(8000)
    void testIsClosed_whenLexerIsNotClosed_returnsFalse() {
        when(lexerMock.isClosed()).thenReturn(false);
        assertFalse(csvParser.isClosed());
        verify(lexerMock).isClosed();
    }
}