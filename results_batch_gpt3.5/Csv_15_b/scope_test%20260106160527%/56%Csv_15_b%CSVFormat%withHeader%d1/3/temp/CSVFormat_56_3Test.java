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

public class CSVFormat_56_3Test {

    @Test
    @Timeout(8000)
    public void testWithHeaderNull() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat format = baseFormat.withHeader((String[]) null);
        assertNotNull(format);
        assertNull(format.getHeader());
        // The returned object should be a new instance, not the same as base
        assertNotSame(baseFormat, format);
        // Other properties should remain unchanged
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
    public void testWithHeaderEmpty() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat format = baseFormat.withHeader(new String[0]);
        assertNotNull(format);
        assertNotNull(format.getHeader());
        assertEquals(0, format.getHeader().length);
        assertNotSame(baseFormat, format);
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderNonEmpty() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        String[] header = new String[] {"col1", "col2", "col3"};
        CSVFormat format = baseFormat.withHeader(header);
        assertNotNull(format);
        assertArrayEquals(header, format.getHeader());
        assertNotSame(baseFormat, format);
    }

    @Test
    @Timeout(8000)
    public void testWithHeaderDoesNotModifyOriginal() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        String[] header = new String[] {"a", "b"};
        CSVFormat format = baseFormat.withHeader(header);
        assertNull(baseFormat.getHeader());
        assertArrayEquals(header, format.getHeader());
    }
}