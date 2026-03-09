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
import java.io.FileOutputStream;
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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrintFileCharset() throws IOException {
        File file = File.createTempFile("test", ".csv");
        file.deleteOnExit();
        CSVPrinter printer = csvFormat.print(file, StandardCharsets.UTF_8);
        assertNotNull(printer);
        printer.close();
    }

    @Test
    @Timeout(8000)
    void testPrintFileCharsetIOException() {
        CSVFormat spyFormat = Mockito.spy(csvFormat);
        File file = new File("/invalid_path/test.csv");
        assertThrows(IOException.class, () -> spyFormat.print(file, StandardCharsets.UTF_8));
    }

    @Test
    @Timeout(8000)
    void testPrintPrivatePrintMethod() throws Exception {
        Appendable appendable = new StringBuilder();

        // The private method signature is:
        // private void print(Object object, CharSequence value, int offset, int len, Appendable out, boolean newRecord)
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        // Test with null object and null CharSequence
        printMethod.invoke(csvFormat, null, null, 0, 0, appendable, true);
        assertEquals("", appendable.toString());

        // Test with non-null CharSequence and newRecord true
        String testValue = "testvalue";
        StringBuilder sb = new StringBuilder();
        // Adjust length argument to be within bounds of the string "testvalue" (length 9)
        printMethod.invoke(csvFormat, "test", testValue, 0, testValue.length(), sb, true);
        assertTrue(sb.length() > 0);

        // Test with newRecord false
        StringBuilder sb2 = new StringBuilder();
        printMethod.invoke(csvFormat, "test", testValue, 0, testValue.length(), sb2, false);
        assertTrue(sb2.length() > 0);
    }

    @Test
    @Timeout(8000)
    void testPrintPrivatePrintAndEscapeMethod() throws Exception {
        Appendable appendable = new StringBuilder();

        // private void printAndEscape(CharSequence value, int offset, int len, Appendable out)
        Method printAndEscape = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        printAndEscape.setAccessible(true);

        String escapeTest = "escapeTest";
        // "escapeTest" length is 10, so offset 0 and len 10 is valid
        printAndEscape.invoke(csvFormat, escapeTest, 0, escapeTest.length(), appendable);
        assertTrue(appendable.toString().length() > 0);
    }

    @Test
    @Timeout(8000)
    void testPrintPrivatePrintAndQuoteMethod() throws Exception {
        Appendable appendable = new StringBuilder();

        // private void printAndQuote(Object object, CharSequence value, int offset, int len, Appendable out, boolean newRecord)
        Method printAndQuote = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printAndQuote.setAccessible(true);

        String quotedValue = "quotedValue";
        // "quotedValue" length is 11, offset 0 and len 11 is valid
        printAndQuote.invoke(csvFormat, "obj", quotedValue, 0, quotedValue.length(), appendable, true);
        assertTrue(appendable.toString().length() > 0);
    }
}