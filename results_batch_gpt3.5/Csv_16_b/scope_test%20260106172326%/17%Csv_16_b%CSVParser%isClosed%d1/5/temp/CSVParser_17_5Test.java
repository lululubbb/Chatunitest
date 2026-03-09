package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserIsClosedTest {

    private CSVParser csvParser;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        // Mock Lexer
        lexerMock = mock(Lexer.class);

        // Create CSVParser instance normally
        Reader dummyReader = new java.io.StringReader("");
        CSVFormat dummyFormat = CSVFormat.DEFAULT;

        csvParser = new CSVParser(dummyReader, dummyFormat);

        // Replace private final lexer field with mock using reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from lexer field if necessary
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(csvParser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void testIsClosedReturnsTrue() {
        when(lexerMock.isClosed()).thenReturn(true);
        Assertions.assertTrue(csvParser.isClosed());
    }

    @Test
    @Timeout(8000)
    void testIsClosedReturnsFalse() {
        when(lexerMock.isClosed()).thenReturn(false);
        Assertions.assertFalse(csvParser.isClosed());
    }
}