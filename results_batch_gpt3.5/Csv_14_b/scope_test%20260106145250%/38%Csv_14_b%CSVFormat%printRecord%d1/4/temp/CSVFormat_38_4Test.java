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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintRecordTest {

    private CSVFormat csvFormat;
    private Appendable appendable;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_noValues() throws IOException {
        csvFormat.printRecord(appendable);
        verify(appendable, times(1)).append('\n');
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_singleValue() throws IOException {
        csvFormat.printRecord(appendable, "value1");
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
        verify(appendable, times(1)).append('\n');
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_multipleValues() throws IOException {
        csvFormat.printRecord(appendable, "value1", "value2", "value3");
        verify(appendable, atLeast(3)).append(any(CharSequence.class));
        verify(appendable, times(1)).append('\n');
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNullValue() throws IOException {
        csvFormat.printRecord(appendable, (Object) null);
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
        verify(appendable, times(1)).append('\n');
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintPrivateMethod() throws Exception {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(csvFormat, "testValue", appendable, true);
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));

        printMethod.invoke(csvFormat, "testValue", appendable, false);
        verify(appendable, atLeast(2)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintAndQuotePrivateMethod() throws Exception {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        CSVFormat csvFormatWithQuote = CSVFormat.DEFAULT.withQuote('"');
        printMethod.invoke(csvFormatWithQuote, "value,with,commas", appendable, true);
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintAndEscapePrivateMethod() throws Exception {
        Method printAndEscapeMethod = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        printAndEscapeMethod.setAccessible(true);
        CharSequence seq = "valueWithEscape\\";
        printAndEscapeMethod.invoke(csvFormat, seq, 0, seq.length(), appendable);
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintPrivateMethod_withNullString() throws Exception {
        CSVFormat csvFormatWithNullString = CSVFormat.DEFAULT.withNullString("NULL");
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        printMethod.invoke(csvFormatWithNullString, null, appendable, true);
        verify(appendable, atLeastOnce()).append("NULL");
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintPrivateMethod_withEmptyString() throws Exception {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        printMethod.invoke(csvFormat, "", appendable, true);
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintln() throws Exception {
        Method printlnMethod = CSVFormat.class.getDeclaredMethod("println", Appendable.class);
        printlnMethod.setAccessible(true);

        printlnMethod.invoke(csvFormat, appendable);
        verify(appendable, times(1)).append('\n');
    }
}