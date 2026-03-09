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

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_10_3Test {

    @Test
    @Timeout(8000)
    void testClose_whenLexerIsNotNull_callsLexerClose() throws Exception {
        // Create a real CSVParser instance with a mock Lexer set via reflection
        CSVParser parser = Mockito.mock(CSVParser.class, Mockito.CALLS_REAL_METHODS);

        // Create a mock Lexer
        Lexer lexerMock = mock(Lexer.class);

        // Use reflection to set the private final lexer field to the mock Lexer
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier on lexer field if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(parser, lexerMock);

        // Call close()
        parser.close();

        // Verify lexer.close() was called once
        verify(lexerMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void testClose_whenLexerIsNull_doesNotThrow() throws Exception {
        CSVParser parser = Mockito.mock(CSVParser.class, Mockito.CALLS_REAL_METHODS);

        // Set lexer field to null via reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier on lexer field if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(parser, null);

        // close() should not throw exception when lexer is null
        assertDoesNotThrow(() -> parser.close());
    }
}