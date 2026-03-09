package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Closeable;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserCloseTest {

    private CSVParser parser;
    private Closeable lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        // Create a dummy CSVFormat (can be null since not used in close)
        CSVFormat format = mock(CSVFormat.class);

        // Create a dummy Reader (can be null since not used in close)
        Reader reader = mock(Reader.class);

        // Instantiate CSVParser with dummy reader and format
        parser = new CSVParser(reader, format);

        // Create a mock Lexer to inject
        // Lexer is package-private or private, so mock as Closeable for close()
        lexerMock = mock(Closeable.class);

        // Use reflection to set the private final lexer field in CSVParser
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier on lexer field if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(parser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void testClose_LexerNotNull_InvokesLexerClose() throws Exception {
        // Call close on parser
        parser.close();

        // Verify lexer.close() was called once
        verify(lexerMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void testClose_LexerNull_DoesNotThrow() throws Exception {
        // Set lexer field to null using reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier on lexer field if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(parser, null);

        // Call close on parser - should not throw
        assertDoesNotThrow(() -> parser.close());
    }
}