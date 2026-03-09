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

class CSVFormat_67_6Test {

    @Test
    @Timeout(8000)
    void testWithQuoteMode_NullQuoteMode() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withQuoteMode(null);
        assertNotNull(updated);
        assertNull(updated.getQuoteMode());
        // Original should remain unchanged
        assertNull(original.getQuoteMode());
        assertNotSame(original, updated);
    }

    @Test
    @Timeout(8000)
    void testWithQuoteMode_NonNullQuoteMode() {
        CSVFormat original = CSVFormat.DEFAULT;
        QuoteMode qm = QuoteMode.ALL_NON_NULL;
        CSVFormat updated = original.withQuoteMode(qm);
        assertNotNull(updated);
        assertEquals(qm, updated.getQuoteMode());
        // Original should remain unchanged
        assertNull(original.getQuoteMode());
        assertNotSame(original, updated);
    }

    @Test
    @Timeout(8000)
    void testWithQuoteMode_Immutability() {
        CSVFormat original = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        CSVFormat updated = original.withQuoteMode(QuoteMode.MINIMAL);
        assertNotSame(original, updated);
        assertEquals(QuoteMode.ALL, original.getQuoteMode());
        assertEquals(QuoteMode.MINIMAL, updated.getQuoteMode());
    }
}