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
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;
    private Charset charset;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        charset = Charset.forName("UTF-8");
    }

    @Test
    @Timeout(8000)
    void testPrint_FileAndCharset_Success() throws IOException {
        File tempFile = File.createTempFile("csvformattest", ".csv");
        tempFile.deleteOnExit();

        CSVPrinter printer = csvFormat.print(tempFile, charset);
        assertNotNull(printer);
        printer.close();
    }

    @Test
    @Timeout(8000)
    void testPrint_FileAndCharset_IOException() {
        File dir = new File(System.getProperty("java.io.tmpdir"));
        assertTrue(dir.isDirectory());

        assertThrows(IOException.class, () -> csvFormat.print(dir, charset));
    }

    @Test
    @Timeout(8000)
    void testPrint_PrivatePrintMethod_Reflection() throws Exception {
        // The actual private method signature is:
        // private void print(Object format, CharSequence value, int from, int to, Appendable out, boolean trailing)
        Method privatePrintMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        privatePrintMethod.setAccessible(true);

        StringBuilder out = new StringBuilder();

        // invoke with null value (CharSequence) but non-null Appendable and valid indices
        privatePrintMethod.invoke(csvFormat, csvFormat, null, 0, 0, out, false);
        assertEquals("", out.toString());

        String testValue = "value123";
        privatePrintMethod.invoke(csvFormat, csvFormat, testValue, 0, testValue.length(), out, true);
        assertTrue(out.length() > 0);

        out.setLength(0);
        privatePrintMethod.invoke(csvFormat, csvFormat, testValue, 1, 3, out, false);
        assertTrue(out.length() > 0);

        out.setLength(0);
        privatePrintMethod.invoke(csvFormat, csvFormat, testValue, 0, testValue.length(), out, true);
        assertTrue(out.toString().endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_PrivateMethod_Reflection() throws Exception {
        Method printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        printAndEscapeMethod.setAccessible(true);

        StringBuilder out = new StringBuilder();

        printAndEscapeMethod.invoke(csvFormat, "abc,def", 0, 7, out);
        assertTrue(out.length() > 0);

        out.setLength(0);
        printAndEscapeMethod.invoke(csvFormat, "", 0, 0, out);
        assertEquals(0, out.length());

        out.setLength(0);
        printAndEscapeMethod.invoke(csvFormat, "abcdef", 2, 3, out);
        assertTrue(out.length() > 0);
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_PrivateMethod_Reflection() throws Exception {
        Method printAndQuoteMethod = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printAndQuoteMethod.setAccessible(true);

        StringBuilder out = new StringBuilder();

        printAndQuoteMethod.invoke(csvFormat, csvFormat, "quotedValue", 0, 11, out, false);
        assertTrue(out.length() > 0);

        out.setLength(0);
        printAndQuoteMethod.invoke(csvFormat, csvFormat, "quotedValue", 0, 11, out, true);
        assertTrue(out.toString().endsWith(csvFormat.getRecordSeparator()));

        out.setLength(0);
        printAndQuoteMethod.invoke(csvFormat, csvFormat, null, 0, 0, out, false);
        assertTrue(out.length() > 0);
    }
}