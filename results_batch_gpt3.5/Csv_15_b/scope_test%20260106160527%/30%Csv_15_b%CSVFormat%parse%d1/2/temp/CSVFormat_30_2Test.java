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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_30_2Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testParse_withValidReader_returnsCSVParser() throws IOException, ReflectiveOperationException {
        Reader mockReader = mock(Reader.class);
        CSVParser parser = csvFormat.parse(mockReader);
        assertNotNull(parser);

        // Use reflection to get the private 'input' field from CSVParser
        Field inputField = CSVParser.class.getDeclaredField("input");
        inputField.setAccessible(true);
        Object readerValue = inputField.get(parser);
        assertEquals(mockReader, readerValue);

        // Use reflection to get the private 'format' field from CSVParser
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object formatValue = formatField.get(parser);
        assertEquals(csvFormat, formatValue);
    }

    @Test
    @Timeout(8000)
    void testParse_withNullReader_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> csvFormat.parse(null));
    }

    @Test
    @Timeout(8000)
    void testParse_multipleCalls_returnDifferentInstances() throws IOException, ReflectiveOperationException {
        Reader mockReader1 = mock(Reader.class);
        Reader mockReader2 = mock(Reader.class);

        CSVParser parser1 = csvFormat.parse(mockReader1);
        CSVParser parser2 = csvFormat.parse(mockReader2);

        assertNotSame(parser1, parser2);

        // Use reflection to get the private 'input' field from CSVParser
        Field inputField = CSVParser.class.getDeclaredField("input");
        inputField.setAccessible(true);
        Object readerValue1 = inputField.get(parser1);
        Object readerValue2 = inputField.get(parser2);

        assertEquals(mockReader1, readerValue1);
        assertEquals(mockReader2, readerValue2);
    }
}