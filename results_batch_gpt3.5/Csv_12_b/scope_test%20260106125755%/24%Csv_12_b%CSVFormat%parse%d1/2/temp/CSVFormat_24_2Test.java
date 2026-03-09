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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_24_2Test {

    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    public void testParse_withValidReader_returnsCSVParser() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = format.parse(mockReader);
        assertNotNull(parser);
        assertSame(mockReader, getReaderFromParser(parser));
        assertEquals(format, getFormatFromParser(parser));
    }

    @Test
    @Timeout(8000)
    public void testParse_withDifferentFormat_returnsCSVParser() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';').withQuote('\'');
        CSVParser parser = format.parse(mockReader);
        assertNotNull(parser);
        assertSame(mockReader, getReaderFromParser(parser));
        assertEquals(format, getFormatFromParser(parser));
    }

    // Use reflection to access private fields of CSVParser to verify constructor parameters
    private Reader getReaderFromParser(CSVParser parser) {
        try {
            Field field = CSVParser.class.getDeclaredField("input");
            field.setAccessible(true);
            return (Reader) field.get(parser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CSVFormat getFormatFromParser(CSVParser parser) {
        try {
            Field field = CSVParser.class.getDeclaredField("format");
            field.setAccessible(true);
            return (CSVFormat) field.get(parser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}