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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVFormatParseTest {

    @Mock
    private Reader mockReader;

    private CSVFormat csvFormat;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);
        csvFormat = CSVFormat.DEFAULT;
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void testParseReturnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVParser parser = csvFormat.parse(mockReader);
        assertNotNull(parser);

        // Use reflection to get the private final CSVFormat format field inside CSVParser
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat parserFormat = (CSVFormat) formatField.get(parser);

        assertEquals(csvFormat, parserFormat);
    }

    @Test
    @Timeout(8000)
    void testParseWithDifferentFormat() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVFormat format = CSVFormat.EXCEL;
        CSVParser parser = format.parse(mockReader);
        assertNotNull(parser);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat parserFormat = (CSVFormat) formatField.get(parser);

        assertEquals(format, parserFormat);
    }

    @Test
    @Timeout(8000)
    void testParseThrowsIOException() throws IOException {
        doThrow(new IOException("Test IOException")).when(mockReader).close();
        Reader reader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Read error");
            }
            @Override
            public void close() throws IOException {
                throw new IOException("Close error");
            }
        };
        CSVFormat format = CSVFormat.DEFAULT;
        assertThrows(IOException.class, () -> format.parse(reader));
    }
}