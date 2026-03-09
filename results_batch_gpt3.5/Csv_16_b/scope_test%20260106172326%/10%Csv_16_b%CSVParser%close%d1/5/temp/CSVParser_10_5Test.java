package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import java.io.Closeable;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_10_5Test {

    @Test
    @Timeout(8000)
    void testClose_whenLexerIsNotNull_callsLexerClose() throws Exception {
        // Arrange
        CSVParser parser = Mockito.mock(CSVParser.class, Mockito.CALLS_REAL_METHODS);

        // Create a mock Lexer with close method
        Class<?> lexerClass = getLexerClass();
        Object lexerMock = Mockito.mock(lexerClass);

        // Use reflection to set private lexer field
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);

        // Act
        parser.close();

        // Assert
        // Verify that lexerMock.close() was called once
        verify((Closeable) lexerMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void testClose_whenLexerIsNull_doesNotThrow() throws Exception {
        // Arrange
        CSVParser parser = Mockito.mock(CSVParser.class, Mockito.CALLS_REAL_METHODS);
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, null);

        // Act & Assert
        assertDoesNotThrow(() -> parser.close());
    }

    private Class<?> getLexerClass() throws ClassNotFoundException {
        // Lexer is a package-private or private class inside org.apache.commons.csv
        // We try to load it by name
        return Class.forName("org.apache.commons.csv.Lexer");
    }
}