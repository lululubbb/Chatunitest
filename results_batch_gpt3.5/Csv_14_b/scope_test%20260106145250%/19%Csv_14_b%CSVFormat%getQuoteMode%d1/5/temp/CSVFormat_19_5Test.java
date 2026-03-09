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

public class CSVFormat_19_5Test {

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode mode = format.getQuoteMode();
        assertNotNull(mode);
        assertEquals(QuoteMode.MINIMAL, mode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_Excel() {
        CSVFormat format = CSVFormat.EXCEL;
        QuoteMode mode = format.getQuoteMode();
        assertNotNull(mode);
        assertEquals(QuoteMode.MINIMAL, mode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_InformixUnload() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD;
        QuoteMode mode = format.getQuoteMode();
        assertNotNull(mode);
        assertEquals(QuoteMode.MINIMAL, mode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_InformixUnloadCsv() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD_CSV;
        QuoteMode mode = format.getQuoteMode();
        assertNotNull(mode);
        assertEquals(QuoteMode.MINIMAL, mode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_Mysql() {
        CSVFormat format = CSVFormat.MYSQL;
        QuoteMode mode = format.getQuoteMode();
        // MYSQL format has quoteCharacter == null, so quoteMode should be null
        assertNull(mode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_Rfc4180() {
        CSVFormat format = CSVFormat.RFC4180;
        QuoteMode mode = format.getQuoteMode();
        assertNotNull(mode);
        assertEquals(QuoteMode.MINIMAL, mode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_Tdf() {
        CSVFormat format = CSVFormat.TDF;
        QuoteMode mode = format.getQuoteMode();
        assertNotNull(mode);
        assertEquals(QuoteMode.MINIMAL, mode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_CustomQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        QuoteMode mode = format.getQuoteMode();
        assertNotNull(mode);
        assertEquals(QuoteMode.ALL, mode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_CustomNullQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(null);
        QuoteMode mode = format.getQuoteMode();
        assertNull(mode);
    }
}