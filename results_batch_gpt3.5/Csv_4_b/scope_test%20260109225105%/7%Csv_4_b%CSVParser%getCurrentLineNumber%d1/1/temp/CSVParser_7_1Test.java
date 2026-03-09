package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.io.StringReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_7_1Test {

    private CSVParser parser;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        // Provide a dummy Reader and CSVFormat since null causes constructor failure
        parser = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);

        // Mock Lexer
        lexerMock = mock(Lexer.class);

        // Inject mocked lexer into parser using reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        lexerField.set(parser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void testGetCurrentLineNumber_ReturnsLexerLineNumber() {
        long expectedLineNumber = 42L;
        when(lexerMock.getCurrentLineNumber()).thenReturn(expectedLineNumber);

        long actualLineNumber = parser.getCurrentLineNumber();

        assertEquals(expectedLineNumber, actualLineNumber);
    }

    @Test
    @Timeout(8000)
    void testGetCurrentLineNumber_ReturnsZeroWhenLexerReturnsZero() {
        long expectedLineNumber = 0L;
        when(lexerMock.getCurrentLineNumber()).thenReturn(expectedLineNumber);

        long actualLineNumber = parser.getCurrentLineNumber();

        assertEquals(expectedLineNumber, actualLineNumber);
    }
}