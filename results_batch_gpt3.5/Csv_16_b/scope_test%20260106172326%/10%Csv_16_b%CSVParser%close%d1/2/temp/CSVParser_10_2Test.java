package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
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

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserCloseTest {

    private CSVParser parser;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        parser = new CSVParser(new java.io.StringReader("a,b,c\n1,2,3"), format);
        lexerMock = mock(Lexer.class);

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);
    }

    @AfterEach
    void tearDown() {
        parser = null;
        lexerMock = null;
    }

    @Test
    @Timeout(8000)
    void close_LexerNotNull_ClosesLexer() throws IOException {
        parser.close();
        verify(lexerMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_LexerIsNull_NoException() throws Exception {
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, null);

        parser.close();
    }
}