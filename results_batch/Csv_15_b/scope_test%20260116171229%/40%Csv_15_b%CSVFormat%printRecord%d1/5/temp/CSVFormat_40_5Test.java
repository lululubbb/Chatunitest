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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
    void testPrintRecordWithNoValues() throws IOException {
        csvFormat.printRecord(appendable);
        // Verify println called once (printRecord calls println after loop)
        verify(appendable, times(1)).append('\r');
        verify(appendable, times(1)).append('\n');
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithSingleValue() throws IOException {
        String value = "test";
        CSVFormat spyFormat = Mockito.spy(csvFormat);
        spyFormat.printRecord(appendable, value);

        // Use reflection to verify private print(Object, Appendable, boolean) called once with newRecord=true
        try {
            Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
            printMethod.setAccessible(true);
            verify(spyFormat, times(1)).print(value, appendable, true);
        } catch (NoSuchMethodException e) {
            // fallback: verify append calls on appendable
            verify(appendable, atLeastOnce()).append(any(CharSequence.class));
        }

        // Verify println called once
        verify(spyFormat, times(1)).println(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithMultipleValues() throws IOException {
        Object[] values = new Object[] { "one", "two", 3, null };
        CSVFormat spyFormat = Mockito.spy(csvFormat);
        spyFormat.printRecord(appendable, values);

        // Use reflection to verify private print(Object, Appendable, boolean) called for each value
        try {
            Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
            printMethod.setAccessible(true);

            verify(spyFormat, times(1)).print("one", appendable, true);
            verify(spyFormat, times(1)).print("two", appendable, false);
            verify(spyFormat, times(1)).print(3, appendable, false);
            verify(spyFormat, times(1)).print(null, appendable, false);
        } catch (NoSuchMethodException e) {
            // fallback: verify append calls on appendable
            verify(appendable, atLeast(values.length)).append(any(CharSequence.class));
        }

        // Verify println called once
        verify(spyFormat, times(1)).println(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithNullAppendableThrowsException() {
        assertThrows(NullPointerException.class, () -> csvFormat.printRecord(null, "value"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordInvokePrivatePrintMethod() throws Exception {
        Object[] values = new Object[] { "a", "b" };
        Appendable out = mock(Appendable.class);

        // Use reflection to invoke private print(Object, Appendable, boolean)
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(csvFormat, values[0], out, true);
        printMethod.invoke(csvFormat, values[1], out, false);

        // Verify append calls on Appendable (at least one append call per print)
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordInvokePrivatePrintAndEscape() throws Exception {
        // test private printAndEscape(CharSequence, int, int, Appendable)
        Method printAndEscape = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class,
                Appendable.class);
        printAndEscape.setAccessible(true);

        Appendable out = mock(Appendable.class);
        CharSequence value = "escape,test";

        printAndEscape.invoke(csvFormat, value, 0, value.length(), out);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordInvokePrivatePrintAndQuote() throws Exception {
        // test private printAndQuote(Object, CharSequence, int, int, Appendable, boolean)
        Method printAndQuote = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printAndQuote.setAccessible(true);

        Appendable out = mock(Appendable.class);
        CharSequence value = "quote,test";

        printAndQuote.invoke(csvFormat, "obj", value, 0, value.length(), out, true);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordInvokePrivatePrintOverload() throws Exception {
        // test private print(Object, CharSequence, int, int, Appendable, boolean)
        Method printOverload = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class,
                Appendable.class, boolean.class);
        printOverload.setAccessible(true);

        Appendable out = mock(Appendable.class);
        CharSequence value = "value";

        printOverload.invoke(csvFormat, "obj", value, 0, value.length(), out, true);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithEmptyStringValue() throws IOException {
        CSVFormat spyFormat = Mockito.spy(csvFormat);
        spyFormat.printRecord(appendable, "");
        verify(spyFormat, times(1)).print("", appendable, true);
        verify(spyFormat, times(1)).println(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithVariousTypes() throws IOException {
        Object[] values = new Object[] { 123, 45.67, true, 'c', new Object() {
            @Override
            public String toString() {
                return "customObj";
            }
        } };
        CSVFormat spyFormat = Mockito.spy(csvFormat);
        spyFormat.printRecord(appendable, values);
        verify(spyFormat, times(values.length)).print(any(), eq(appendable), anyBoolean());
        verify(spyFormat, times(1)).println(appendable);
    }
}