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
import org.junit.jupiter.api.Test;

class CSVParser_6_6Test {

    @Test
    @Timeout(8000)
    void close_whenLexerIsNotNull_shouldCloseLexer() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVParser parser = new CSVParser(null, null);

        Lexer lexerMock = mock(Lexer.class);

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier via reflection using Java 12+ compatible approach
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        lexerField.set(parser, lexerMock);

        parser.close();

        verify(lexerMock).close();
    }

    @Test
    @Timeout(8000)
    void close_whenLexerIsNull_shouldNotThrow() throws IOException {
        CSVParser parser = new CSVParser(null, null);

        // lexer field is null by default
        parser.close();
    }
}