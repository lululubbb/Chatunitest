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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrintReturnsCSVPrinter() throws IOException, ReflectiveOperationException {
        Appendable out = new StringBuilder();
        CSVPrinter printer = csvFormat.print(out);
        assertNotNull(printer);

        // Access private field 'out' in CSVPrinter via reflection
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object printerOut = outField.get(printer);
        assertSame(out, printerOut);

        // Access private field 'format' in CSVPrinter via reflection
        Field formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object printerFormat = formatField.get(printer);
        assertSame(csvFormat, printerFormat);
    }

    @Test
    @Timeout(8000)
    void testPrintWithMockAppendable() throws IOException, ReflectiveOperationException {
        Appendable mockOut = mock(Appendable.class);
        CSVPrinter printer = csvFormat.print(mockOut);
        assertNotNull(printer);

        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object printerOut = outField.get(printer);
        assertSame(mockOut, printerOut);

        Field formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object printerFormat = formatField.get(printer);
        assertSame(csvFormat, printerFormat);
    }

    @Test
    @Timeout(8000)
    void testPrintPrivatePrintMethodReflection() throws Exception {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withQuote('"').withDelimiter(';').withRecordSeparator("\n");

        // Access private print method with signature:
        // private void print(Object object, CharSequence value, int offset, int len, Appendable out, boolean newRecord)
        Method privatePrintMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        privatePrintMethod.setAccessible(true);

        String testValue = "test,value";
        String charSequenceValue = "test,value";
        int offset = 0;
        int len = charSequenceValue.length();
        boolean newRecord = true;

        // Invoke private print method
        privatePrintMethod.invoke(format, testValue, charSequenceValue, offset, len, out, newRecord);

        String output = out.toString();
        assertNotNull(output);
        assertTrue(output.contains("test,value") || output.contains("\"test,value\""));
    }

    @Test
    @Timeout(8000)
    void testPrintPrivatePrintAndEscapeMethodReflection() throws Exception {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');

        Method privatePrintAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        privatePrintAndEscapeMethod.setAccessible(true);

        String value = "escape\\value";
        privatePrintAndEscapeMethod.invoke(format, value, 0, value.length(), out);

        String output = out.toString();
        assertNotNull(output);
        assertTrue(output.contains("escape") && output.contains("\\"));
    }

    @Test
    @Timeout(8000)
    void testPrintPrivatePrintAndQuoteMethodReflection() throws Exception {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withQuote('"');

        Method privatePrintAndQuoteMethod = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        privatePrintAndQuoteMethod.setAccessible(true);

        String value = "quote,value";
        privatePrintAndQuoteMethod.invoke(format, value, value, 0, value.length(), out, true);

        String output = out.toString();
        assertNotNull(output);
        assertTrue(output.startsWith("\"") && output.endsWith("\""));
    }
}