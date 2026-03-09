package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_30_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testParse_withValidReader_returnsCSVParser() throws IOException {
        Reader reader = mock(Reader.class);

        CSVParser parser = null;
        try {
            Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
            constructor.setAccessible(true);
            parser = constructor.newInstance(reader, csvFormat);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail("Reflection failed: " + e.getMessage());
        }

        assertNotNull(parser);
        CSVParser actualParser = csvFormat.parse(reader);
        assertNotNull(actualParser);
        assertEquals(parser.getClass(), actualParser.getClass());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullReader_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> csvFormat.parse(null));
    }

    @Test
    @Timeout(8000)
    void testParse_withCustomCSVFormat() throws IOException {
        CSVFormat customFormat = CSVFormat.DEFAULT.withDelimiter(';').withQuote('"');
        Reader reader = mock(Reader.class);

        CSVParser parser = null;
        try {
            Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
            constructor.setAccessible(true);
            parser = constructor.newInstance(reader, customFormat);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail("Reflection failed: " + e.getMessage());
        }

        assertNotNull(parser);
        CSVParser actualParser = customFormat.parse(reader);
        assertNotNull(actualParser);
        assertEquals(parser.getClass(), actualParser.getClass());
    }

    @Test
    @Timeout(8000)
    void testParse_invokesCSVParserConstructor() throws IOException {
        Reader reader = mock(Reader.class);

        CSVParser parser = null;
        try {
            Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
            constructor.setAccessible(true);
            parser = constructor.newInstance(reader, csvFormat);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail("Reflection failed: " + e.getMessage());
        }

        assertNotNull(parser);
        CSVParser actualParser = csvFormat.parse(reader);
        assertNotNull(actualParser);
        assertEquals(parser.getClass(), actualParser.getClass());
    }
}