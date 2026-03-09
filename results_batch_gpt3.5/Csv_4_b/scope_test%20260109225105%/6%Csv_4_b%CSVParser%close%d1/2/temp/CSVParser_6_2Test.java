package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.File;
import java.io.FileReader;
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

import static org.mockito.Mockito.*;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_6_2Test {

    private CSVParser csvParser;
    private Closeable mockLexer;

    @BeforeEach
    void setUp() throws Exception {
        // Create a mock Lexer that implements Closeable
        mockLexer = mock(Closeable.class);

        // Use reflection to create CSVParser instance with dummy Reader and CSVFormat
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(java.io.Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        csvParser = constructor.newInstance(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Use reflection to set private final lexer field
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier on lexer field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(csvParser, mockLexer);
    }

    @Test
    @Timeout(8000)
    void close_callsLexerClose_whenLexerNotNull() throws IOException {
        csvParser.close();
        verify(mockLexer, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_doesNotThrow_whenLexerIsNull() throws Exception {
        // Set lexer to null
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier on lexer field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(csvParser, null);

        assertDoesNotThrow(() -> csvParser.close());
    }

    @Test
    @Timeout(8000)
    void close_throwsIOException_whenLexerCloseThrows() throws Exception {
        doThrow(new IOException("close failed")).when(mockLexer).close();

        IOException thrown = assertThrows(IOException.class, () -> csvParser.close());
        assertEquals("close failed", thrown.getMessage());
    }
}