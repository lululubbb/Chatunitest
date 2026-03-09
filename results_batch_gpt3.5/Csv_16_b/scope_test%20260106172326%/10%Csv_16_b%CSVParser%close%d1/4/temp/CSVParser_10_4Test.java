package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.Lexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVParserCloseTest {

    private CSVParser csvParser;
    private Lexer mockLexer;

    @BeforeEach
    void setUp() throws Exception {
        // Create a real CSVParser instance with a dummy Reader and CSVFormat
        csvParser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Create a mock Lexer
        mockLexer = mock(Lexer.class);

        // Use reflection to set the private final lexer field
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from the lexer field if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        lexerField.set(csvParser, mockLexer);
    }

    @Test
    @Timeout(8000)
    void testClose_LexerNotNull() throws IOException {
        // Call close
        csvParser.close();

        // Verify lexer.close() was called once
        verify(mockLexer, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void testClose_LexerNull() throws Exception {
        // Set lexer to null via reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from the lexer field if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        lexerField.set(csvParser, null);

        // Call close, should not throw
        assertDoesNotThrow(() -> csvParser.close());
    }
}