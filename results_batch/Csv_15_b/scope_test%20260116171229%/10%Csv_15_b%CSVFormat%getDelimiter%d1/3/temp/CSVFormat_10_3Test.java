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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CSVFormat_10_3Test {

    @Test
    @Timeout(8000)
    void testGetDelimiter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Excel() {
        CSVFormat format = CSVFormat.EXCEL;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_InformixUnload() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD;
        assertEquals('|', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_InformixUnloadCsv() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD_CSV;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Mysql() {
        CSVFormat format = CSVFormat.MYSQL;
        assertEquals('\t', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_PostgresqlCsv() {
        CSVFormat format = CSVFormat.POSTGRESQL_CSV;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_PostgresqlText() {
        CSVFormat format = CSVFormat.POSTGRESQL_TEXT;
        assertEquals('\t', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Rfc4180() {
        CSVFormat format = CSVFormat.RFC4180;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Tdf() {
        CSVFormat format = CSVFormat.TDF;
        assertEquals('\t', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_CustomFormat() throws Exception {
        // Use reflection to invoke the private constructor to create a custom CSVFormat with delimiter ';'
        java.lang.reflect.Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class, Object[].class, String[].class, boolean.class, boolean.class,
                boolean.class, boolean.class, boolean.class, boolean.class);
        ctor.setAccessible(true);
        CSVFormat custom = ctor.newInstance(
                ';', // delimiter
                null, // quoteChar
                null, // quoteMode
                null, // commentStart
                null, // escape
                false, // ignoreSurroundingSpaces
                false, // ignoreEmptyLines
                null, // recordSeparator
                null, // nullString
                null, // headerComments
                null, // header
                false, // skipHeaderRecord
                false, // allowMissingColumnNames
                false, // ignoreHeaderCase
                false, // trim
                false, // trailingDelimiter
                false // autoFlush
        );
        assertEquals(';', custom.getDelimiter());
    }
}