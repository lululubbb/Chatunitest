package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CSVFormat_29_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testParse_withValidReader_returnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        String csvData = "a,b,c\n1,2,3\n4,5,6";
        Reader reader = new StringReader(csvData);

        CSVParser parser = csvFormat.parse(reader);

        assertNotNull(parser);
        // Use reflection to get the private 'format' field from CSVParser
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat formatValue = (CSVFormat) formatField.get(parser);
        assertEquals(csvFormat, formatValue);
    }

    @Test
    @Timeout(8000)
    void testParse_withMockedReader_invokesCSVParserConstructor() throws IOException, NoSuchFieldException, IllegalAccessException {
        Reader mockReader = mock(Reader.class);

        CSVParser parser = csvFormat.parse(mockReader);

        assertNotNull(parser);
        // Use reflection to get the private 'format' field from CSVParser
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat formatValue = (CSVFormat) formatField.get(parser);
        assertEquals(csvFormat, formatValue);
    }

    @Test
    @Timeout(8000)
    void testParse_throwsIOException_whenReaderThrows() {
        Reader failingReader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Forced IOException");
            }

            @Override
            public void close() throws IOException {
            }
        };

        assertThrows(IOException.class, () -> csvFormat.parse(failingReader));
    }
}