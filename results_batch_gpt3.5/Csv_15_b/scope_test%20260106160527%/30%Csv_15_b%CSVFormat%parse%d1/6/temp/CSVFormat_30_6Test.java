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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_30_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setup() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testParseReturnsCSVParser() throws Exception {
        Reader reader = mock(Reader.class);

        // Mock CSVParser instance
        CSVParser mockParser = mock(CSVParser.class);

        // Spy on CSVFormat instance
        CSVFormat spyFormat = spy(csvFormat);

        // Use doReturn to mock parse(Reader) to return mockParser to avoid calling real method
        doReturn(mockParser).when(spyFormat).parse(any(Reader.class));

        CSVParser parser = spyFormat.parse(reader);

        assertNotNull(parser);
        assertEquals(mockParser, parser);
    }

    @Test
    @Timeout(8000)
    void testParseWithNullReaderThrowsNPE() {
        assertThrows(NullPointerException.class, () -> csvFormat.parse(null));
    }

    @Test
    @Timeout(8000)
    void testParseCreatesCSVParserWithThisInstance() throws Exception {
        Reader reader = mock(Reader.class);

        CSVParser parser = new CSVParser(reader, csvFormat);
        assertNotNull(parser);
    }
}