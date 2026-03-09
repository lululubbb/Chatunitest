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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_11_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = spy(new CSVPrinter(out, format));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        Object[] values = new Object[]{"value1", 123, null, "last"};
        CSVPrinter spyPrinter = printer;

        spyPrinter.printRecord(values);

        InOrder inOrder = inOrder(spyPrinter);
        for (Object value : values) {
            inOrder.verify(spyPrinter).print(value);
        }
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNoValues() throws IOException {
        CSVPrinter spyPrinter = printer;

        spyPrinter.printRecord();

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintPrivateMethodsViaReflection() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);

        // Test print(Object, CharSequence, int, int)
        printMethod.invoke(printer, "testObject", "testValue", 0, 9);

        // Test printAndEscape(CharSequence, int, int)
        printAndEscapeMethod.invoke(printer, "escapeValue", 0, 11);

        // Test printAndQuote(Object, CharSequence, int, int)
        printAndQuoteMethod.invoke(printer, "quoteObject", "quoteValue", 0, 10);
    }
}