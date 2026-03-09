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

class CSVFormat_64_2Test {

    @Test
    @Timeout(8000)
    void testWithQuote_validChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = '\'';

        CSVFormat newFormat = format.withQuote(quoteChar);

        assertNotNull(newFormat);
        assertEquals(quoteChar, newFormat.getQuoteCharacter());
        // Original format unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_nullChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = null;

        CSVFormat newFormat = format.withQuote(quoteChar);

        assertNotNull(newFormat);
        assertNull(newFormat.getQuoteCharacter());
        // Original format unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_lineBreakChar_CR() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = '\r';

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            format.withQuote(quoteChar);
        });
        assertEquals("The quoteChar cannot be a line break", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_lineBreakChar_LF() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = '\n';

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            format.withQuote(quoteChar);
        });
        assertEquals("The quoteChar cannot be a line break", ex.getMessage());
    }
}