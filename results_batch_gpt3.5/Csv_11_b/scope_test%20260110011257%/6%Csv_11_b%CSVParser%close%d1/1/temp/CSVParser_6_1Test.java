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

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.Lexer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_6_1Test {

    @Test
    @Timeout(8000)
    void close_whenLexerIsNotNull_invokesLexerClose() throws Exception {
        CSVParser parser = spy(new CSVParser(mock(java.io.Reader.class), CSVFormat.DEFAULT));
        Lexer lexer = mock(Lexer.class);
        setLexerField(parser, lexer);

        parser.close();

        verify(lexer).close();
    }

    @Test
    @Timeout(8000)
    void close_whenLexerIsNull_doesNotThrow() throws Exception {
        CSVParser parser = spy(new CSVParser(mock(java.io.Reader.class), CSVFormat.DEFAULT));
        setLexerField(parser, null);

        assertDoesNotThrow(() -> parser.close());
    }

    private void setLexerField(CSVParser parser, Lexer lexer) throws Exception {
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from the lexer field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(parser, lexer);
    }
}