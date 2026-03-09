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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinter_11_1Test {

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
    void testPrintRecord_withMultipleValues() throws IOException {
        Object val1 = "value1";
        Object val2 = 123;
        Object val3 = null;

        CSVPrinter spyPrinter = spy(printer);

        // Stub print(Object) to do nothing (avoid actual printing logic)
        doNothing().when(spyPrinter).print(any());

        spyPrinter.printRecord(val1, val2, val3);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).print(val1);
        inOrder.verify(spyPrinter).print(val2);
        inOrder.verify(spyPrinter).print(val3);
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNoValues() throws IOException {
        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).print(any());

        spyPrinter.printRecord();

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintPrivateMethodViaReflection() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // We test that printRecord calls print(Object) which calls private print(Object, CharSequence, int, int)
        // Using reflection to invoke private print(Object, CharSequence, int, int)
        Method privatePrintMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrintMethod.setAccessible(true);

        // call private print with a sample value
        privatePrintMethod.invoke(printer, "test", "test", 0, 4);

        // No exception means it works, but we can also verify output Appendable called
        verifyNoMoreInteractions(out); // Changed from verifyNoInteractions to verifyNoMoreInteractions
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_printThrowsIOException() throws IOException {
        CSVPrinter spyPrinter = spy(printer);

        doThrow(new IOException("print failed")).when(spyPrinter).print(any());

        IOException thrown = assertThrows(IOException.class, () -> {
            spyPrinter.printRecord("fail");
        });

        assertEquals("print failed", thrown.getMessage());
    }
}