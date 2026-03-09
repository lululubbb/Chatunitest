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

public class CSVFormat_56_2Test {

    @Test
    @Timeout(8000)
    public void testWithHeader_NullHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((String[]) null);
        assertNotNull(result);
        assertNull(result.getHeader());
        // Other fields remain the same
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteCharacter(), result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_EmptyHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(new String[0]);
        assertNotNull(result);
        // The header is empty array, but getHeader() returns null internally for empty headers
        assertNull(result.getHeader());
        // Other fields remain the same
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteCharacter(), result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_NonEmptyHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] header = new String[] {"col1", "col2", "col3"};
        CSVFormat result = format.withHeader(header);
        assertNotNull(result);
        assertArrayEquals(header, result.getHeader());
        // Other fields remain the same
        assertEquals(format.getDelimiter(), result.getDelimiter());
        assertEquals(format.getQuoteCharacter(), result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithHeader_OriginalFormatUnchanged() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] header = new String[] {"a", "b"};
        CSVFormat result = format.withHeader(header);
        assertNotSame(format, result);
        assertNull(format.getHeader());
        assertArrayEquals(header, result.getHeader());
    }
}