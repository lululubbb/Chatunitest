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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_24_5Test {

    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockReader = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    public void testParseReturnsCSVParser() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = format.parse(mockReader);
        assertNotNull(parser);
        // Verify that CSVParser was created with the Reader and this CSVFormat instance
        assertEquals(format, getCSVParserFormat(parser));
    }

    @Test
    @Timeout(8000)
    public void testParseWithNullReaderThrowsException() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertThrows(NullPointerException.class, () -> {
            format.parse(null);
        });
    }

    // Helper method to access private 'format' field inside CSVParser via reflection
    private CSVFormat getCSVParserFormat(CSVParser parser) {
        try {
            java.lang.reflect.Field formatField = CSVParser.class.getDeclaredField("format");
            formatField.setAccessible(true);
            return (CSVFormat) formatField.get(parser);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection on CSVParser.format failed: " + e.getMessage());
            return null;
        }
    }
}