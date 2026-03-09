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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.lang.reflect.Field;

class CSVFormatWithIgnoreEmptyLinesTest {

    private CSVFormat baseFormat;

    @BeforeEach
    void setUp() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesTrue() throws Exception {
        CSVFormat format = baseFormat.withIgnoreEmptyLines(true);
        assertNotNull(format);
        assertTrue(format.getIgnoreEmptyLines());
        // Original format should be unchanged
        assertTrue(baseFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesFalse() throws Exception {
        CSVFormat format = baseFormat.withIgnoreEmptyLines(false);
        assertNotNull(format);
        assertFalse(format.getIgnoreEmptyLines());
        // Original format should be unchanged
        assertTrue(baseFormat.getIgnoreEmptyLines());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesCreatesNewInstance() throws Exception {
        CSVFormat format1 = baseFormat.withIgnoreEmptyLines(true);
        CSVFormat format2 = baseFormat.withIgnoreEmptyLines(false);
        assertNotSame(format1, format2);
        assertNotSame(format1, baseFormat);
        assertNotSame(format2, baseFormat);
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesPreservesOtherFields() throws Exception {
        CSVFormat format = baseFormat.withIgnoreEmptyLines(false);

        assertEquals(baseFormat.getDelimiter(), format.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), format.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), format.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), format.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), format.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), format.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getRecordSeparator(), format.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), format.getNullString());
        assertArrayEquals(baseFormat.getHeader(), format.getHeader());
        assertArrayEquals(baseFormat.getHeaderComments(), format.getHeaderComments());
        assertEquals(baseFormat.getSkipHeaderRecord(), format.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), format.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLinesReflectionCheck() throws Exception {
        CSVFormat format = baseFormat.withIgnoreEmptyLines(false);
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean value = ignoreEmptyLinesField.getBoolean(format);
        assertFalse(value);
    }
}