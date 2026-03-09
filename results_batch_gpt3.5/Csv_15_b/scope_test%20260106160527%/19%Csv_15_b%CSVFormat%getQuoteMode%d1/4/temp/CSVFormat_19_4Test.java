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

class CSVFormat_19_4Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals(QuoteMode.MINIMAL, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_excel() {
        CSVFormat format = CSVFormat.EXCEL;
        assertEquals(QuoteMode.MINIMAL, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_informixUnload() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD;
        assertEquals(QuoteMode.MINIMAL, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_informixUnloadCsv() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD_CSV;
        assertEquals(QuoteMode.MINIMAL, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_mysql() {
        CSVFormat format = CSVFormat.MYSQL;
        assertEquals(QuoteMode.ALL_NON_NULL, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_postgresqlCsv() {
        CSVFormat format = CSVFormat.POSTGRESQL_CSV;
        assertEquals(QuoteMode.ALL_NON_NULL, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_postgresqlText() {
        CSVFormat format = CSVFormat.POSTGRESQL_TEXT;
        assertEquals(QuoteMode.ALL_NON_NULL, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_rfc4180() {
        CSVFormat format = CSVFormat.RFC4180;
        assertEquals(QuoteMode.MINIMAL, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_tdf() {
        CSVFormat format = CSVFormat.TDF;
        assertEquals(QuoteMode.MINIMAL, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_withQuoteModeMethod() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        assertEquals(QuoteMode.ALL, format.getQuoteMode());
    }

}