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
import org.junit.jupiter.api.Test;

class CSVFormat_65_2Test {

    @Test
    @Timeout(8000)
    void testWithQuoteModeNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withQuoteMode(null);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertNull(newFormat.getQuoteMode());
        // Other properties remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteModeNonNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode mode = QuoteMode.ALL;
        CSVFormat newFormat = format.withQuoteMode(mode);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertEquals(mode, newFormat.getQuoteMode());
        // Other properties remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuoteModeDifferentInstance() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        QuoteMode newMode = QuoteMode.NON_NUMERIC;
        CSVFormat newFormat = format.withQuoteMode(newMode);
        assertNotNull(newFormat);
        assertNotSame(format, newFormat);
        assertEquals(newMode, newFormat.getQuoteMode());
        // Other properties remain unchanged
        assertEquals(format.getDelimiter(), newFormat.getDelimiter());
        assertEquals(format.getQuoteCharacter(), newFormat.getQuoteCharacter());
    }

}