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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_24_4Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testParse_withValidReader_returnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        String csvData = "a,b,c\n1,2,3";
        Reader reader = new StringReader(csvData);

        CSVParser parser = csvFormat.parse(reader);

        assertNotNull(parser);
        CSVFormat formatFromParser = getCSVFormatFromParser(parser);
        assertEquals(csvFormat, formatFromParser);
    }

    @Test
    @Timeout(8000)
    void testParse_withMockedReader_callsCSVParserConstructor() throws IOException, NoSuchFieldException, IllegalAccessException {
        Reader mockReader = mock(Reader.class);
        when(mockReader.read(any(char[].class), anyInt(), anyInt())).thenReturn(-1);

        CSVParser parser = csvFormat.parse(mockReader);

        assertNotNull(parser);
        CSVFormat formatFromParser = getCSVFormatFromParser(parser);
        assertEquals(csvFormat, formatFromParser);
    }

    @Test
    @Timeout(8000)
    void testParse_withNullReader_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> csvFormat.parse(null));
    }

    private CSVFormat getCSVFormatFromParser(CSVParser parser) throws NoSuchFieldException, IllegalAccessException {
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        return (CSVFormat) formatField.get(parser);
    }
}