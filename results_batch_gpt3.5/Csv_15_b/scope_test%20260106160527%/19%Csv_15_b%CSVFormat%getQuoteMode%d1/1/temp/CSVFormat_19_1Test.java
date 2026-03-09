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
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

class CSVFormat_19_1Test {

    private QuoteMode getQuoteModeField(CSVFormat format) {
        try {
            Field field = CSVFormat.class.getDeclaredField("quoteMode");
            field.setAccessible(true);
            return (QuoteMode) field.get(format);
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
            return null; // unreachable
        }
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_DefaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode expected = getQuoteModeField(format);
        assertSame(expected, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_ExcelInstance() {
        CSVFormat format = CSVFormat.EXCEL;
        QuoteMode expected = getQuoteModeField(format);
        assertSame(expected, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_InformixUnloadInstance() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD;
        QuoteMode expected = getQuoteModeField(format);
        assertSame(expected, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_MySQLInstance() {
        CSVFormat format = CSVFormat.MYSQL;
        QuoteMode expected = getQuoteModeField(format);
        assertSame(expected, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_PostgreSQLCsvInstance() {
        CSVFormat format = CSVFormat.POSTGRESQL_CSV;
        QuoteMode expected = getQuoteModeField(format);
        assertSame(expected, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_PostgreSQLTextInstance() {
        CSVFormat format = CSVFormat.POSTGRESQL_TEXT;
        QuoteMode expected = getQuoteModeField(format);
        assertSame(expected, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Rfc4180Instance() {
        CSVFormat format = CSVFormat.RFC4180;
        QuoteMode expected = getQuoteModeField(format);
        assertSame(expected, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_TdfInstance() {
        CSVFormat format = CSVFormat.TDF;
        QuoteMode expected = getQuoteModeField(format);
        assertSame(expected, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_CustomInstance_withQuoteModeNull() throws Exception {
        // Using reflection to create a CSVFormat instance with quoteMode null
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                ',', '"', null, null, null, false, false, "\n", null,
                null, null, false, false, false, false, false, false);
        assertNull(format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithMockito() throws Exception {
        CSVFormat format = Mockito.mock(CSVFormat.class, Mockito.CALLS_REAL_METHODS);
        QuoteMode qm = QuoteMode.ALL;
        Field field = CSVFormat.class.getDeclaredField("quoteMode");
        field.setAccessible(true);
        // Set the field on the underlying mock handler target, not on the proxy object itself
        // Use Mockito's spy to avoid proxy issues or use reflection on the mock's invocation handler
        // But simpler is to spy an actual instance
        CSVFormat spyFormat = Mockito.spy(CSVFormat.DEFAULT);
        field.set(spyFormat, qm);
        Mockito.when(spyFormat.getQuoteMode()).thenCallRealMethod();
        assertSame(qm, spyFormat.getQuoteMode());
    }
}