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

import java.lang.reflect.Field;

class CSVFormat_19_5Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Default() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode quoteMode = getQuoteModeUsingReflection(format);
        assertNull(quoteMode, "Default CSVFormat should have null quoteMode");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithQuoteModeAllNonNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL_NON_NULL);
        QuoteMode quoteMode = getQuoteModeUsingReflection(format);
        assertEquals(QuoteMode.ALL_NON_NULL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithQuoteModeMinimal() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        QuoteMode quoteMode = getQuoteModeUsingReflection(format);
        assertEquals(QuoteMode.MINIMAL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithQuoteModeNone() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.NONE);
        QuoteMode quoteMode = getQuoteModeUsingReflection(format);
        assertEquals(QuoteMode.NONE, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithQuoteModeNonNumeric() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.NON_NUMERIC);
        QuoteMode quoteMode = getQuoteModeUsingReflection(format);
        assertEquals(QuoteMode.NON_NUMERIC, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_ExcelConstant() throws Exception {
        CSVFormat format = CSVFormat.EXCEL;
        QuoteMode quoteMode = getQuoteModeUsingReflection(format);
        assertNull(quoteMode, "EXCEL CSVFormat should have null quoteMode");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_MySQLConstant() throws Exception {
        CSVFormat format = CSVFormat.MYSQL;
        QuoteMode quoteMode = getQuoteModeUsingReflection(format);
        assertEquals(QuoteMode.ALL_NON_NULL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_PostgreSQLCSVConstant() throws Exception {
        CSVFormat format = CSVFormat.POSTGRESQL_CSV;
        QuoteMode quoteMode = getQuoteModeUsingReflection(format);
        assertEquals(QuoteMode.ALL_NON_NULL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_PostgreSQLTextConstant() throws Exception {
        CSVFormat format = CSVFormat.POSTGRESQL_TEXT;
        QuoteMode quoteMode = getQuoteModeUsingReflection(format);
        assertEquals(QuoteMode.ALL_NON_NULL, quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_InformixUnloadConstant() throws Exception {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD;
        QuoteMode quoteMode = getQuoteModeUsingReflection(format);
        assertNull(quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_InformixUnloadCsvConstant() throws Exception {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD_CSV;
        QuoteMode quoteMode = getQuoteModeUsingReflection(format);
        assertNull(quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_RFC4180Constant() throws Exception {
        CSVFormat format = CSVFormat.RFC4180;
        QuoteMode quoteMode = getQuoteModeUsingReflection(format);
        assertNull(quoteMode);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_TDFConstant() throws Exception {
        CSVFormat format = CSVFormat.TDF;
        QuoteMode quoteMode = getQuoteModeUsingReflection(format);
        assertNull(quoteMode);
    }

    private QuoteMode getQuoteModeUsingReflection(CSVFormat format) throws Exception {
        Field quoteModeField = null;
        Class<?> clazz = format.getClass();
        // Traverse class hierarchy to find the field if subclassed
        while (clazz != null) {
            try {
                quoteModeField = clazz.getDeclaredField("quoteMode");
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        if (quoteModeField == null) {
            throw new NoSuchFieldException("Field 'quoteMode' not found in class hierarchy");
        }
        quoteModeField.setAccessible(true);
        return (QuoteMode) quoteModeField.get(format);
    }
}