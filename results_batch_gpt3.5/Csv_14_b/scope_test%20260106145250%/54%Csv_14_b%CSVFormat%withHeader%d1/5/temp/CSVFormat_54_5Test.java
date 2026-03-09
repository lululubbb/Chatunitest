package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_54_5Test {

    @Test
    @Timeout(8000)
    void testWithHeaderWithNull() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = baseFormat.withHeader((String[]) null);
        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        // The getHeader() method returns null if header is not set
        assertNull(newFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithEmpty() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = baseFormat.withHeader(new String[0]);
        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        assertNotNull(newFormat.getHeader());
        assertEquals(0, newFormat.getHeader().length);
    }

    @Test
    @Timeout(8000)
    void testWithHeaderWithValues() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        String[] headers = new String[]{"A", "B", "C"};
        CSVFormat newFormat = baseFormat.withHeader(headers);
        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        assertArrayEquals(headers, newFormat.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeaderDoesNotAffectOtherProperties() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withDelimiter(';').withQuote('\'').withIgnoreEmptyLines(false);
        String[] headers = new String[]{"X", "Y"};
        CSVFormat newFormat = baseFormat.withHeader(headers);

        assertEquals(';', newFormat.getDelimiter());
        assertEquals(Character.valueOf('\''), newFormat.getQuoteCharacter());
        assertFalse(newFormat.getIgnoreEmptyLines());
        assertArrayEquals(headers, newFormat.getHeader());
    }
}