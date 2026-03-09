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

public class CSVFormat_23_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testParse_withValidReader_returnsCSVParser() throws IOException {
        Reader reader = mock(Reader.class);
        // Do not mock reader behavior since CSVParser reads from it internally
        CSVParser parser = csvFormat.parse(reader);
        assertNotNull(parser);
        assertEquals(csvFormat, getCSVFormatFromParser(parser));
    }

    @Test
    @Timeout(8000)
    public void testParse_withNullReader_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> csvFormat.parse(null));
    }

    // Helper method to access CSVFormat field inside CSVParser using reflection
    private CSVFormat getCSVFormatFromParser(CSVParser parser) {
        try {
            Field field = CSVParser.class.getDeclaredField("format");
            field.setAccessible(true);
            return (CSVFormat) field.get(parser);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed to get CSVFormat from CSVParser: " + e.getMessage());
            return null;
        }
    }
}