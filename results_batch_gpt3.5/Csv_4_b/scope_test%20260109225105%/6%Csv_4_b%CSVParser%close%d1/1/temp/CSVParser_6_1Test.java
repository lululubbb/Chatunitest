package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_6_1Test {

    private CSVParser parser;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        // Create a CSVParser instance with a dummy Reader and a non-null CSVFormat
        parser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Create a mock Lexer
        lexerMock = mock(Lexer.class);

        // Use reflection to set the private final lexer field to the mock
        setFinalField(parser, "lexer", lexerMock);
    }

    @Test
    @Timeout(8000)
    void close_lexerNotNull_callsLexerClose() throws IOException {
        // Call the close method
        parser.close();

        // Verify lexer.close() was called once
        verify(lexerMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_lexerNull_noExceptionThrown() throws Exception {
        // Set lexer field to null via reflection
        setFinalField(parser, "lexer", null);

        // Call close and assert no exception thrown
        parser.close();
    }

    private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVParser.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }
}