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

class CSVPrinter_1_6Test {

    private Appendable appendable;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        appendable = new StringBuilder();
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_withValidParams_shouldInitialize() throws IOException {
        doNothing().when(format).validate();

        CSVPrinter printer = new CSVPrinter(appendable, format);

        assertNotNull(printer);
        assertSame(appendable, printer.getOut());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNullOut_shouldThrow() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(null, format));
        assertEquals("out", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNullFormat_shouldThrow() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(appendable, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_whenFormatValidateThrows_shouldThrow() throws IOException {
        doThrow(new IOException("validate failed")).when(format).validate();

        IOException ex = assertThrows(IOException.class, () -> new CSVPrinter(appendable, format));
        assertEquals("validate failed", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrint_methods() throws Exception {
        doNothing().when(format).validate();
        CSVPrinter printer = new CSVPrinter(appendable, format);

        // Access private print(Object, CharSequence, int, int)
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);
        printMethod.invoke(printer, "obj", "value", 0, "value".length());

        // Access private printAndEscape(CharSequence, int, int)
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);
        printAndEscapeMethod.invoke(printer, "escapeValue", 0, "escapeValue".length());

        // Access private printAndQuote(Object, CharSequence, int, int)
        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);
        printAndQuoteMethod.invoke(printer, "obj", "quoteValue", 0, "quoteValue".length());
    }
}