package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
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

import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.TAB;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CSVFormat_10_5Test {

    @Test
    @Timeout(8000)
    void testGetDelimiter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals(COMMA, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Excel() {
        CSVFormat format = CSVFormat.EXCEL;
        assertEquals(COMMA, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_InformixUnload() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD;
        assertEquals(PIPE, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_InformixUnloadCsv() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD_CSV;
        assertEquals(COMMA, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Mysql() {
        CSVFormat format = CSVFormat.MYSQL;
        assertEquals(TAB, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_PostgresqlCsv() {
        CSVFormat format = CSVFormat.POSTGRESQL_CSV;
        assertEquals(COMMA, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_PostgresqlText() {
        CSVFormat format = CSVFormat.POSTGRESQL_TEXT;
        assertEquals(TAB, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Rfc4180() {
        CSVFormat format = CSVFormat.RFC4180;
        assertEquals(COMMA, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Tdf() {
        CSVFormat format = CSVFormat.TDF;
        assertEquals(TAB, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_CustomDelimiter() {
        CSVFormat customFormat = CSVFormat.newFormat(';');
        assertEquals(';', customFormat.getDelimiter());
    }
}