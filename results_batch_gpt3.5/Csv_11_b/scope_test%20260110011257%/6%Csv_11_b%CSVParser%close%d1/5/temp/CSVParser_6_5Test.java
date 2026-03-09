package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_6_5Test {

    private CSVParser parser;
    private Lexer lexerMock;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a mock Lexer
        lexerMock = mock(Lexer.class);

        // Create a real CSVParser instance using a StringReader and a default CSVFormat
        parser = new CSVParser(new java.io.StringReader("a,b,c\n1,2,3"), CSVFormat.DEFAULT);

        // Use reflection to set the private final lexer field to the mock
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from lexer field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(parser, lexerMock);
    }

    @Test
    @Timeout(8000)
    public void testClose_LexerNotNull_ClosesLexer() throws IOException {
        parser.close();
        verify(lexerMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    public void testClose_LexerNull_NoException() throws Exception {
        // Set lexer to null
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from lexer field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(parser, null);

        assertDoesNotThrow(() -> parser.close());
    }
}