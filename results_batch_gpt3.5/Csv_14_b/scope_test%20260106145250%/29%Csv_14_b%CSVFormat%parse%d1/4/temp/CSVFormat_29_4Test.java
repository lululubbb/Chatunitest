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
import java.io.StringReader;
import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;

class CSVFormat_29_4Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testParse_withValidReader_shouldReturnCSVParserInstance() throws IOException {
        String csvData = "a,b,c\n1,2,3\n4,5,6";
        Reader reader = new StringReader(csvData);

        CSVParser parser = csvFormat.parse(reader);

        assertNotNull(parser);
        // Since CSVParser constructor is called directly, check that parser has expected format and reader
        // We cannot access private fields, so just check class type
        assertEquals(CSVParser.class, parser.getClass());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullReader_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> csvFormat.parse(null));
    }

    @Test
    @Timeout(8000)
    void testParse_invokesCSVParserConstructor() throws Exception {
        Reader reader = new StringReader("a,b,c\n1,2,3");

        // Instead of subclassing CSVFormat (which is final and has a private constructor),
        // create a spy of CSVFormat.DEFAULT and override parse method via Mockito spy.
        CSVFormat spyFormat = spy(csvFormat);

        doAnswer((InvocationOnMock invocation) -> {
            Reader in = invocation.getArgument(0, Reader.class);
            Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
            constructor.setAccessible(true);
            CSVParser realParser = constructor.newInstance(in, spyFormat);
            CSVParser spyParser = spy(realParser);
            return spyParser;
        }).when(spyFormat).parse(any(Reader.class));

        CSVParser parser = spyFormat.parse(reader);

        assertNotNull(parser);
        assertEquals(CSVParser.class, parser.getClass());
        verify(parser, atLeastOnce()).iterator(); // Use a method that exists in CSVParser to verify interaction
    }
}