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

class CSVFormatWithQuoteModeTest {

    @Test
    @Timeout(8000)
    void testWithQuoteModeNull() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat changed = original.withQuoteMode(null);
        assertNotNull(changed);
        assertNull(changed.getQuoteMode());
        // Original should remain unchanged
        assertNull(original.getQuoteMode());
        assertNotSame(original, changed);
    }

    @Test
    @Timeout(8000)
    void testWithQuoteModeNonNull() {
        CSVFormat original = CSVFormat.DEFAULT;
        QuoteMode mode = QuoteMode.ALL;
        CSVFormat changed = original.withQuoteMode(mode);
        assertNotNull(changed);
        assertEquals(mode, changed.getQuoteMode());
        // Original should remain unchanged
        assertNull(original.getQuoteMode());
        assertNotSame(original, changed);
    }

    @Test
    @Timeout(8000)
    void testWithQuoteModeDoesNotAffectOtherProperties() {
        CSVFormat original = CSVFormat.DEFAULT.withDelimiter(';').withQuote('\'').withIgnoreEmptyLines(false)
                .withRecordSeparator("\n").withNullString("NULL").withHeader("A", "B").withSkipHeaderRecord(true)
                .withAllowMissingColumnNames(true).withIgnoreHeaderCase(true).withTrim(true).withTrailingDelimiter(true);
        QuoteMode mode = QuoteMode.MINIMAL;
        CSVFormat changed = original.withQuoteMode(mode);

        assertNotNull(changed);
        assertEquals(mode, changed.getQuoteMode());
        assertEquals(original.getDelimiter(), changed.getDelimiter());
        assertEquals(original.getQuoteCharacter(), changed.getQuoteCharacter());
        assertEquals(original.getIgnoreEmptyLines(), changed.getIgnoreEmptyLines());
        assertEquals(original.getRecordSeparator(), changed.getRecordSeparator());
        assertEquals(original.getNullString(), changed.getNullString());
        assertArrayEquals(original.getHeader(), changed.getHeader());
        assertEquals(original.getSkipHeaderRecord(), changed.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), changed.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), changed.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), changed.getTrim());
        assertEquals(original.getTrailingDelimiter(), changed.getTrailingDelimiter());
    }
}