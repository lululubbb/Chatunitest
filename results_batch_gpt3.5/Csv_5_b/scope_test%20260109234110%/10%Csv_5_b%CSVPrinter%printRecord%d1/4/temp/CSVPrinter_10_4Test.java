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
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinter_10_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithEmptyIterable() throws IOException {
        printer.printRecord(Collections.emptyList());
        // Should call println() once, no print() calls
        verify(out, times(1)).append('\n');
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithSingleValue() throws IOException {
        Iterable<String> values = Collections.singletonList("value");
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecord(values);

        InOrder inOrder = inOrder(spyPrinter, out);
        inOrder.verify(spyPrinter).print("value");
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithMultipleValues() throws IOException {
        Iterable<Object> values = Arrays.asList("a", 123, null);
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecord(values);

        InOrder inOrder = inOrder(spyPrinter, out);
        inOrder.verify(spyPrinter).print("a");
        inOrder.verify(spyPrinter).print(123);
        inOrder.verify(spyPrinter).print((Object) null);
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintPrivatePrintObjectCharSequenceOffsetLen() throws Throwable {
        String testValue = "testValue";
        Method method = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        // Call with offset=0, len=full length
        method.invoke(printer, testValue, testValue, 0, testValue.length());

        // We expect that out.append is called at least once (depends on implementation)
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintPrivatePrintAndEscape() throws Throwable {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        String val = "escape\nvalue";
        method.invoke(printer, val, 0, val.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintPrivatePrintAndQuote() throws Throwable {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        String val = "quote\"value";
        method.invoke(printer, val, val, 0, val.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordThrowsIOExceptionFromPrint() throws IOException {
        Iterable<Object> values = Arrays.asList("a", "b");
        CSVPrinter spyPrinter = spy(printer);

        doThrow(new IOException("fail")).when(spyPrinter).print("b");

        IOException thrown = null;
        try {
            spyPrinter.printRecord(values);
        } catch (IOException e) {
            thrown = e;
        }
        assert thrown != null;
        assert "fail".equals(thrown.getMessage());
    }
}