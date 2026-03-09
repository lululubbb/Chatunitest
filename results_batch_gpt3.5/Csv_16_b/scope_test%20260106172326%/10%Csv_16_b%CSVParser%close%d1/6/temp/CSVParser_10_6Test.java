package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
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

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_10_6Test {

    private CSVParser csvParser;
    private Lexer mockLexer;

    @BeforeEach
    void setUp() throws Exception {
        // Create mocks
        Reader mockReader = mock(Reader.class);
        CSVFormat mockFormat = mock(CSVFormat.class);

        // Create a CSVParser instance with mocks
        csvParser = new CSVParser(mockReader, mockFormat);

        mockLexer = mock(Lexer.class);

        // Use reflection to set the private final lexer field
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from the lexer field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        // Set the lexer field directly
        lexerField.set(csvParser, mockLexer);
    }

    @Test
    @Timeout(8000)
    void close_lexerNotNull_closesLexer() throws IOException {
        csvParser.close();
        verify(mockLexer, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_lexerNull_doesNotThrow() throws Exception {
        // Set lexer to null via reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from the lexer field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(csvParser, null);

        assertDoesNotThrow(() -> csvParser.close());
    }
}