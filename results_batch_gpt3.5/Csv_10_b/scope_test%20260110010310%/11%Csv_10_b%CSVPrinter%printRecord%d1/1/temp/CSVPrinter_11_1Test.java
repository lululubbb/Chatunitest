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

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_11_1Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        Object[] values = {"value1", 123, null};

        // Spy printer to verify print(Object) and println() calls
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord(values);

        for (Object value : values) {
            verify(spyPrinter).print(value);
        }
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNoValues() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord();

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrivatePrint_Object_CharSequence_int_int() throws Throwable {
        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrint.setAccessible(true);

        CharSequence cs = "testValue";
        // Call with offset=0, len=cs.length()
        privatePrint.invoke(printer, "object", cs, 0, cs.length());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscape() throws Throwable {
        Method privatePrintAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        privatePrintAndEscape.setAccessible(true);

        CharSequence cs = "escapeValue";
        privatePrintAndEscape.invoke(printer, cs, 0, cs.length());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuote() throws Throwable {
        Method privatePrintAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        privatePrintAndQuote.setAccessible(true);

        CharSequence cs = "quoteValue";
        privatePrintAndQuote.invoke(printer, "object", cs, 0, cs.length());
    }
}