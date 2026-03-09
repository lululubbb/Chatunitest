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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

class CSVFormat_19_6Test {

    @Test
    @Timeout(8000)
    void testGetQuoteModeDefault() {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.MINIMAL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteModeWithCustomQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.ALL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteModeWithNullQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(null);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNull(quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteModeOnVariousPredefinedFormats() {
        assertEquals(QuoteMode.MINIMAL, CSVFormat.EXCEL.getQuoteMode());
        assertEquals(QuoteMode.MINIMAL, CSVFormat.INFORMIX_UNLOAD.getQuoteMode());
        assertEquals(QuoteMode.MINIMAL, CSVFormat.INFORMIX_UNLOAD_CSV.getQuoteMode());
        assertEquals(QuoteMode.ALL_NON_NULL, CSVFormat.MYSQL.getQuoteMode());
        assertEquals(QuoteMode.ALL_NON_NULL, CSVFormat.POSTGRESQL_CSV.getQuoteMode());
        assertEquals(QuoteMode.ALL_NON_NULL, CSVFormat.POSTGRESQL_TEXT.getQuoteMode());
        assertEquals(QuoteMode.MINIMAL, CSVFormat.RFC4180.getQuoteMode());
        assertEquals(QuoteMode.MINIMAL, CSVFormat.TDF.getQuoteMode());
    }
}