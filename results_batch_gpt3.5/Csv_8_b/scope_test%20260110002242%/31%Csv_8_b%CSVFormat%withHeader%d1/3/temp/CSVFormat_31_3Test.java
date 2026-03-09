package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

class CSVFormatWithHeaderTest {

    @Test
    @Timeout(8000)
    public void testWithHeaderCreatesNewInstanceWithGivenHeader() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        String[] headers = new String[]{"Name", "Age", "City"};
        CSVFormat newFormat = baseFormat.withHeader(headers);

        // Verify newFormat is not the same instance as baseFormat
        assertNotSame(baseFormat, newFormat);

        // Verify header is set correctly
        assertArrayEquals(headers, newFormat.getHeader());

        // Verify other fields are copied correctly from baseFormat
        assertEquals(baseFormat.getDelimiter(), newFormat.getDelimiter());
        assertEquals(baseFormat.getQuoteChar(), newFormat.getQuoteChar());
        assertEquals(baseFormat.getQuotePolicy(), newFormat.getQuotePolicy());
        assertEquals(baseFormat.getCommentStart(), newFormat.getCommentStart());
        assertEquals(baseFormat.getEscape(), newFormat.getEscape());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), newFormat.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), newFormat.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), newFormat.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), newFormat.getNullString());
        assertEquals(baseFormat.getSkipHeaderRecord(), newFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderNullHeaderCreatesNewInstanceWithNullHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        CSVFormat newFormat = baseFormat.withHeader((String[]) null);

        assertNotSame(baseFormat, newFormat);
        assertNull(newFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderEmptyHeaderCreatesNewInstanceWithEmptyHeader() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        CSVFormat newFormat = baseFormat.withHeader(new String[0]);

        assertNotSame(baseFormat, newFormat);
        assertNotNull(newFormat.getHeader());
        assertEquals(0, newFormat.getHeader().length);
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderReflectionInvocation() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        String[] headers = new String[]{"A", "B"};

        Method withHeaderMethod = CSVFormat.class.getMethod("withHeader", String[].class);

        CSVFormat newFormat = (CSVFormat) withHeaderMethod.invoke(baseFormat, (Object) headers);

        assertNotNull(newFormat);
        assertArrayEquals(headers, newFormat.getHeader());
    }
}