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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_6_3Test {

    private CSVParser csvParser;
    private Closeable mockLexer;

    @BeforeEach
    void setUp() throws Exception {
        // Create a real CSVParser instance with a dummy Reader and format using constructor directly
        csvParser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Create a mock lexer (Closeable)
        mockLexer = mock(Closeable.class);

        // Use reflection to set the private final lexer field
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier if present (works on Java 8)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(csvParser, mockLexer);
    }

    @Test
    @Timeout(8000)
    void close_whenLexerIsNotNull_shouldCallLexerClose() throws IOException {
        csvParser.close();
        verify(mockLexer, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_whenLexerIsNull_shouldNotThrow() throws Exception {
        // Set lexer field to null via reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(csvParser, null);

        assertDoesNotThrow(() -> csvParser.close());
    }

}