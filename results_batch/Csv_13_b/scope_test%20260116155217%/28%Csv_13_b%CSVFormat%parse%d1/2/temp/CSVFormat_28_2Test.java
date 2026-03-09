package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Serializable;
import java.io.StringWriter;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_28_2Test {

    private Reader readerMock;

    @BeforeEach
    public void setUp() {
        readerMock = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    public void testParseReturnsCSVParserInstance() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Mockito Reader mock needs to return -1 on read to simulate EOF and avoid IOException on parse
        when(readerMock.read(any(char[].class), anyInt(), anyInt())).thenReturn(-1);

        CSVParser parser = format.parse(readerMock);

        assertNotNull(parser);
        assertEquals(CSVParser.class, parser.getClass());
    }

    @Test
    @Timeout(8000)
    public void testParseThrowsIOException() {
        CSVFormat format = CSVFormat.DEFAULT;

        Reader throwingReader = new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException("Mock IOException");
            }

            @Override
            public void close() throws IOException {
                // no-op
            }
        };

        assertThrows(IOException.class, () -> {
            format.parse(throwingReader);
        });
    }
}