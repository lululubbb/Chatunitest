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
import org.junit.jupiter.api.Test;

class CSVFormat_18_2Test {

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('"', quoteChar.charValue());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_Excel() {
        CSVFormat format = CSVFormat.EXCEL;
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('"', quoteChar.charValue());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_InformixUnload() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD;
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('"', quoteChar.charValue());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_InformixUnloadCsv() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD_CSV;
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('"', quoteChar.charValue());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_Mysql_NullQuote() {
        CSVFormat format = CSVFormat.MYSQL;
        Character quoteChar = format.getQuoteCharacter();
        assertNull(quoteChar);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_PostgresqlCsv() {
        CSVFormat format = CSVFormat.POSTGRESQL_CSV;
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('"', quoteChar.charValue());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_PostgresqlText() {
        CSVFormat format = CSVFormat.POSTGRESQL_TEXT;
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('"', quoteChar.charValue());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_Rfc4180() {
        CSVFormat format = CSVFormat.RFC4180;
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('"', quoteChar.charValue());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_Tdf() {
        CSVFormat format = CSVFormat.TDF;
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('"', quoteChar.charValue());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_CustomFormat_WithNullQuote() {
        CSVFormat format = CSVFormat.newFormat(';').withQuote((Character) null);
        Character quoteChar = format.getQuoteCharacter();
        assertNull(quoteChar);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_CustomFormat_WithQuote() {
        CSVFormat format = CSVFormat.newFormat(';').withQuote(Character.valueOf('\''));
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('\'', quoteChar.charValue());
    }
}