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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVFormat_30_3Test {

    @Mock
    private Reader mockReader;

    private CSVFormat csvFormat;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
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
    void testParseReturnsCSVParser() throws IOException, Exception {
        CSVParser parser = csvFormat.parse(mockReader);
        assertNotNull(parser, "CSVParser should not be null");

        Field readerField = CSVParser.class.getDeclaredField("reader");
        readerField.setAccessible(true);
        Reader actualReader = (Reader) readerField.get(parser);
        assertEquals(mockReader, actualReader, "Reader passed should be the one used by CSVParser");

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat actualFormat = (CSVFormat) formatField.get(parser);
        assertEquals(csvFormat, actualFormat, "CSVFormat passed should be the one used by CSVParser");
    }

    @Test
    @Timeout(8000)
    void testParseThrowsIOException() throws IOException {
        doThrow(new IOException("Test IOException")).when(mockReader).read(any(char[].class), anyInt(), anyInt());
        assertThrows(IOException.class, () -> csvFormat.parse(mockReader));
    }

    @Test
    @Timeout(8000)
    void testParseWithDifferentFormats() throws IOException, Exception {
        CSVFormat formatExcel = CSVFormat.EXCEL;
        CSVParser parserExcel = formatExcel.parse(mockReader);
        assertNotNull(parserExcel);

        Field formatFieldExcel = CSVParser.class.getDeclaredField("format");
        formatFieldExcel.setAccessible(true);
        CSVFormat actualFormatExcel = (CSVFormat) formatFieldExcel.get(parserExcel);
        assertEquals(formatExcel, actualFormatExcel);

        CSVFormat formatMysql = CSVFormat.MYSQL;
        CSVParser parserMysql = formatMysql.parse(mockReader);
        assertNotNull(parserMysql);

        Field formatFieldMysql = CSVParser.class.getDeclaredField("format");
        formatFieldMysql.setAccessible(true);
        CSVFormat actualFormatMysql = (CSVFormat) formatFieldMysql.get(parserMysql);
        assertEquals(formatMysql, actualFormatMysql);

        CSVFormat formatTdf = CSVFormat.TDF;
        CSVParser parserTdf = formatTdf.parse(mockReader);
        assertNotNull(parserTdf);

        Field formatFieldTdf = CSVParser.class.getDeclaredField("format");
        formatFieldTdf.setAccessible(true);
        CSVFormat actualFormatTdf = (CSVFormat) formatFieldTdf.get(parserTdf);
        assertEquals(formatTdf, actualFormatTdf);
    }
}