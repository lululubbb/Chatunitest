package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
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
import java.io.Reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_6_4Test {

    private CSVParser csvParser;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        lexerMock = mock(Lexer.class);
        // Provide non-null dummy Reader and CSVFormat to constructor to avoid NPE
        csvParser = new CSVParser(mock(Reader.class), mock(CSVFormat.class));
        // Inject mock lexer into csvParser using reflection
        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(csvParser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void testClose_LexerNotNull() throws IOException {
        csvParser.close();
        verify(lexerMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void testClose_LexerNull() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Set lexer to null to test branch when lexer is null
        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove 'final' modifier from lexer field
        java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        lexerField.set(csvParser, null);

        // Should not throw exception
        csvParser.close();
        // No interaction with lexerMock expected
        verify(lexerMock, never()).close();
    }
}