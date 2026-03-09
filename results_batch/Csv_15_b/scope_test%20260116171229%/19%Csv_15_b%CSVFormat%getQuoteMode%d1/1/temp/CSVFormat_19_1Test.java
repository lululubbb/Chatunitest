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

class CSVFormat_19_1Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Default() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.MINIMAL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Excel() {
        CSVFormat format = CSVFormat.EXCEL.withQuoteMode(QuoteMode.MINIMAL);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.MINIMAL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_InformixUnload() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD.withQuoteMode(QuoteMode.MINIMAL);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.MINIMAL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_MySQL() {
        CSVFormat format = CSVFormat.MYSQL.withQuoteMode(QuoteMode.ALL_NON_NULL);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.ALL_NON_NULL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_PostgreSQLCSV() {
        CSVFormat format = CSVFormat.POSTGRESQL_CSV.withQuoteMode(QuoteMode.ALL_NON_NULL);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.ALL_NON_NULL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_PostgreSQLText() {
        CSVFormat format = CSVFormat.POSTGRESQL_TEXT.withQuoteMode(QuoteMode.ALL_NON_NULL);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.ALL_NON_NULL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_RFC4180() {
        CSVFormat format = CSVFormat.RFC4180.withQuoteMode(QuoteMode.MINIMAL);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.MINIMAL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_TDF() {
        CSVFormat format = CSVFormat.TDF.withQuoteMode(QuoteMode.MINIMAL);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.MINIMAL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_CustomWithQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.ALL, quoteMode);
    }
}