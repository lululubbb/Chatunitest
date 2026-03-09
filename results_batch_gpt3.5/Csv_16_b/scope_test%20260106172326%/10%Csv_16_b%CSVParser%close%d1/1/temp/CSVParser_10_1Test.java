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

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class CSVParser_10_1Test {

    @Test
    @Timeout(8000)
    void testClose_whenLexerIsNotNull_shouldCallLexerClose() throws Exception {
        // Arrange
        CSVFormat mockFormat = mock(CSVFormat.class);
        Reader mockReader = mock(Reader.class);
        CSVParser parser = new CSVParser(mockReader, mockFormat);

        Lexer mockLexer = mock(Lexer.class);

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier if present
        Field modifiersField = null;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException ignored) {
            // Java 12+ does not have 'modifiers' field, skip
        }

        lexerField.set(parser, mockLexer);

        // Act
        Method closeMethod = CSVParser.class.getDeclaredMethod("close");
        closeMethod.setAccessible(true);
        closeMethod.invoke(parser);

        // Assert
        verify(mockLexer, times(1)).close();

        // Restore final modifier if possible
        if (modifiersField != null) {
            modifiersField.setInt(lexerField, lexerField.getModifiers() | Modifier.FINAL);
        }
    }

    @Test
    @Timeout(8000)
    void testClose_whenLexerIsNull_shouldNotThrow() throws Exception {
        // Arrange
        CSVFormat mockFormat = mock(CSVFormat.class);
        Reader mockReader = mock(Reader.class);
        CSVParser parser = new CSVParser(mockReader, mockFormat);

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier if present
        Field modifiersField = null;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException ignored) {
            // Java 12+ does not have 'modifiers' field, skip
        }

        lexerField.set(parser, null);

        // Act & Assert: invoking close should not throw exception
        Method closeMethod = CSVParser.class.getDeclaredMethod("close");
        closeMethod.setAccessible(true);
        closeMethod.invoke(parser);

        // Restore final modifier if possible
        if (modifiersField != null) {
            modifiersField.setInt(lexerField, lexerField.getModifiers() | Modifier.FINAL);
        }
    }
}