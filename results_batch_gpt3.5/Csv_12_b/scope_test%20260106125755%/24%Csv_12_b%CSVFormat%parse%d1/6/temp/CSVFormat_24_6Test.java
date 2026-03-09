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

import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.Test;

class CSVFormat_24_6Test {

    @Test
    @Timeout(8000)
    void testParseReturnsCSVParserInstance() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use a real Reader with some content to avoid issues in CSVParser
        Reader reader = new java.io.StringReader("a,b,c\n1,2,3");
        CSVParser parser = format.parse(reader);
        assertNotNull(parser);
        assertEquals(format, getCSVFormatFromParser(parser));
    }

    @Test
    @Timeout(8000)
    void testParseWithNullReaderThrowsException() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertThrows(NullPointerException.class, () -> format.parse(null));
    }

    // Helper method to use reflection to get the private 'format' field from CSVParser
    private CSVFormat getCSVFormatFromParser(CSVParser parser) {
        try {
            java.lang.reflect.Field formatField = CSVParser.class.getDeclaredField("format");
            formatField.setAccessible(true);
            return (CSVFormat) formatField.get(parser);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed: " + e.getMessage());
            return null;
        }
    }
}