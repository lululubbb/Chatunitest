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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_6_6Test {

    private CSVParser parser;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        lexerMock = mock(Lexer.class);
        parser = new CSVParser(mock(java.io.Reader.class), mock(CSVFormat.class));
        // Use reflection to set the private final lexer field
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void testClose_whenLexerNotNull_callsLexerClose() throws IOException {
        parser.close();
        verify(lexerMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void testClose_whenLexerIsNull_noException() throws Exception {
        // Set lexer field to null via reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, null);

        parser.close();
        // No exception expected, no interactions with lexerMock
        verify(lexerMock, never()).close();
    }
}