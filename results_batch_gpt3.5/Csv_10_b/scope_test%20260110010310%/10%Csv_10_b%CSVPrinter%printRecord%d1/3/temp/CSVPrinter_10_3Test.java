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
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinter_10_3Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = spy(new CSVPrinter(out, format));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        Iterable<Object> values = Arrays.asList("a", 1, null);

        printer.printRecord(values);

        InOrder inOrder = inOrder(printer, out);
        inOrder.verify(printer).print("a");
        inOrder.verify(printer).print(1);
        inOrder.verify(printer).print((Object) null);
        inOrder.verify(printer).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyIterable() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        printer.printRecord(values);

        InOrder inOrder = inOrder(printer, out);
        inOrder.verify(printer, never()).print(any());
        inOrder.verify(printer).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_reflectiveInvocation() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Iterable<Object> values = Arrays.asList("x", "y");

        Method printRecordMethod = CSVPrinter.class.getDeclaredMethod("printRecord", Iterable.class);
        printRecordMethod.setAccessible(true);

        printRecordMethod.invoke(printer, values);

        verify(printer, times(0)).close(); // just sanity check no close called
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }
}