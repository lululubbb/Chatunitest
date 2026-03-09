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
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_6_5Test {

    private CSVParser csvParser;
    private Lexer mockLexer;

    @BeforeEach
    void setUp() throws Exception {
        // Create a CSVParser instance with a dummy Reader and CSVFormat
        Reader dummyReader = mock(Reader.class);
        CSVFormat dummyFormat = mock(CSVFormat.class);

        // Create a CSVParser instance normally
        csvParser = new CSVParser(dummyReader, dummyFormat);

        // Create a mock Lexer and inject it via reflection
        mockLexer = mock(Lexer.class);
        setFinalField(csvParser, "lexer", mockLexer);
    }

    @Test
    @Timeout(8000)
    void close_whenLexerIsNotNull_shouldCloseLexer() throws IOException {
        csvParser.close();
        verify(mockLexer, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_whenLexerIsNull_shouldNotThrow() throws Exception {
        // Set lexer field to null via reflection
        setFinalField(csvParser, "lexer", null);

        // Should not throw any exception
        csvParser.close();
    }

    private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }
}