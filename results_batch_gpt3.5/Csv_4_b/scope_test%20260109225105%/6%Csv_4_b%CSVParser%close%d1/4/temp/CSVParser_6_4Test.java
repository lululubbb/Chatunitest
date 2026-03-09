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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_6_4Test {

    @Test
    @Timeout(8000)
    void close_shouldCloseLexer_whenLexerIsNotNull() throws Exception {
        // Arrange
        CSVParser parser = createCSVParserWithMockLexer();

        Lexer lexer = getLexer(parser);
        assertNotNull(lexer);

        // Act
        parser.close();

        // Assert
        verify(lexer, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_shouldNotThrow_whenLexerIsNull() throws Exception {
        // Arrange
        CSVParser parser = createCSVParserWithNullLexer();

        // Act & Assert
        assertDoesNotThrow(() -> parser.close());
    }

    // Helper method to create CSVParser instance with mocked Lexer
    private CSVParser createCSVParserWithMockLexer() throws Exception {
        CSVParser parser = createCSVParserInstance();
        Lexer mockLexer = mock(Lexer.class);
        setLexer(parser, mockLexer);
        return parser;
    }

    // Helper method to create CSVParser instance with null Lexer
    private CSVParser createCSVParserWithNullLexer() throws Exception {
        CSVParser parser = createCSVParserInstance();
        setLexer(parser, null);
        return parser;
    }

    // Helper to create CSVParser instance via constructor
    private CSVParser createCSVParserInstance() throws Exception {
        Reader dummyReader = mock(Reader.class);
        CSVFormat dummyFormat = mock(CSVFormat.class);
        return new CSVParser(dummyReader, dummyFormat);
    }

    // Reflection helper to set lexer field (which is final)
    private void setLexer(CSVParser parser, Lexer lexer) throws Exception {
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from lexer field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        lexerField.set(parser, lexer);
    }

    // Reflection helper to get lexer field
    private Lexer getLexer(CSVParser parser) throws Exception {
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        return (Lexer) lexerField.get(parser);
    }

    // Dummy Lexer interface to compile
    private interface Lexer {
        void close() throws IOException;
    }
}