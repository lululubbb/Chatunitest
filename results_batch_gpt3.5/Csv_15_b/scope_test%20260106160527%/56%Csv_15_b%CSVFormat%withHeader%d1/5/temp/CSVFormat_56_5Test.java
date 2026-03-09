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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

class CSVFormat_56_5Test {

    private CSVFormat baseFormat;

    @BeforeEach
    void setUp() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithHeader_NullHeader() {
        CSVFormat format = baseFormat.withHeader((String[]) null);
        assertNotNull(format);
        // The getHeader() returns null when header is null
        assertNull(format.getHeader());
        assertEquals(baseFormat.getDelimiter(), format.getDelimiter());
        assertEquals(baseFormat.getQuoteCharacter(), format.getQuoteCharacter());
        assertEquals(baseFormat.getQuoteMode(), format.getQuoteMode());
        assertEquals(baseFormat.getCommentMarker(), format.getCommentMarker());
        assertEquals(baseFormat.getEscapeCharacter(), format.getEscapeCharacter());
        assertEquals(baseFormat.getIgnoreSurroundingSpaces(), format.getIgnoreSurroundingSpaces());
        assertEquals(baseFormat.getIgnoreEmptyLines(), format.getIgnoreEmptyLines());
        assertEquals(baseFormat.getRecordSeparator(), format.getRecordSeparator());
        assertEquals(baseFormat.getNullString(), format.getNullString());
        assertArrayEquals(baseFormat.getHeaderComments(), format.getHeaderComments());
        assertEquals(baseFormat.getSkipHeaderRecord(), format.getSkipHeaderRecord());
        assertEquals(baseFormat.getAllowMissingColumnNames(), format.getAllowMissingColumnNames());
        assertEquals(baseFormat.getIgnoreHeaderCase(), format.getIgnoreHeaderCase());
        assertEquals(baseFormat.getTrim(), format.getTrim());
        assertEquals(baseFormat.getTrailingDelimiter(), format.getTrailingDelimiter());
        assertEquals(baseFormat.getAutoFlush(), format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyHeader() {
        CSVFormat format = baseFormat.withHeader(new String[0]);
        assertNotNull(format);
        assertNotNull(format.getHeader());
        assertEquals(0, format.getHeader().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeader_WithValues() {
        String[] header = new String[] {"col1", "col2", "col3"};
        CSVFormat format = baseFormat.withHeader(header);
        assertNotNull(format);
        assertArrayEquals(header, format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_Immutability() {
        String[] header = new String[] {"A", "B"};
        CSVFormat format1 = baseFormat.withHeader(header);
        CSVFormat format2 = format1.withHeader("X", "Y");
        assertNotSame(format1, format2);
        assertArrayEquals(new String[]{"A", "B"}, format1.getHeader());
        assertArrayEquals(new String[]{"X", "Y"}, format2.getHeader());
    }
}