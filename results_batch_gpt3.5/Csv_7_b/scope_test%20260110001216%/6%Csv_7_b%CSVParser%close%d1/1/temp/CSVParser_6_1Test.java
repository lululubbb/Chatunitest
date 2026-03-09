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
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_6_1Test {

    private CSVParser csvParser;

    @BeforeEach
    void setUp() throws IOException {
        // Provide non-null Reader and CSVFormat to avoid NPE in constructor
        csvParser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);
    }

    @Test
    @Timeout(8000)
    void close_lexerNotNull_shouldCloseLexer() throws IOException, NoSuchFieldException, IllegalAccessException {
        Lexer lexerMock = Mockito.mock(Lexer.class);

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier via reflection if present
        // In recent JDKs, the 'modifiers' field is not accessible; skip if not found
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(lexerField, lexerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        } catch (NoSuchFieldException ignored) {
            // Ignore if not present
        }

        lexerField.set(csvParser, lexerMock);

        csvParser.close();

        verify(lexerMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_lexerNull_shouldNotThrow() throws IOException, NoSuchFieldException, IllegalAccessException {
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier via reflection if present
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(lexerField, lexerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
        } catch (NoSuchFieldException ignored) {
            // Ignore if not present
        }

        lexerField.set(csvParser, null);

        assertDoesNotThrow(() -> csvParser.close());
    }
}