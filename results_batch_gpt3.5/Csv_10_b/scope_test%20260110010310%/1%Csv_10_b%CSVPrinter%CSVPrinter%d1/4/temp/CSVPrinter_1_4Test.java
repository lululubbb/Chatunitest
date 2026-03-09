package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_1_4Test {

    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
        // Since format.validate() is called in constructor and might be final,
        // stub it to do nothing to avoid exceptions
        doNothing().when(format).validate();
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullOut_throwsException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new CSVPrinter(null, format);
        });
        assertEquals("out", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_throwsException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new CSVPrinter(out, null);
        });
        assertEquals("format", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_validParameters_callsFormatValidate() throws IOException {
        CSVPrinter printer = new CSVPrinter(out, format);
        assertNotNull(printer);
        verify(format, times(1)).validate();
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscape_invocation() throws Exception {
        CSVPrinter printer = new CSVPrinter(out, format);
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        String value = "escapeTest";
        method.invoke(printer, value, 0, value.length());
        assertTrue(out.toString().contains("escapeTest"));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuote_invocation() throws Exception {
        CSVPrinter printer = new CSVPrinter(out, format);
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        String value = "quoteTest";
        method.invoke(printer, "obj", value, 0, value.length());
        assertTrue(out.toString().contains("quoteTest"));
    }

    @Test
    @Timeout(8000)
    void testPrivatePrint_invocation() throws Exception {
        CSVPrinter printer = new CSVPrinter(out, format);
        Method method = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        String value = "printTest";
        method.invoke(printer, "obj", value, 0, value.length());
        assertTrue(out.toString().contains("printTest"));
    }
}