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

class CSVPrinter_11_6Test {

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
    void testPrintRecord_withMultipleValues_callsPrintAndPrintln() throws IOException {
        Object value1 = "value1";
        Object value2 = 123;
        Object value3 = null;

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecord(value1, value2, value3);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).print(value1);
        inOrder.verify(spyPrinter).print(value2);
        inOrder.verify(spyPrinter).print(value3);
        inOrder.verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNoValues_callsOnlyPrintln() throws IOException {
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecord();

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrivatePrintMethodWithReflection() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class);
        printMethod.setAccessible(true);

        // Call printRecord with one value and verify print(Object) is invoked via spy
        CSVPrinter spyPrinter = spy(printer);
        Object value = "testValue";

        spyPrinter.printRecord(value);

        verify(spyPrinter).print(value);

        // Directly invoke private print(Object) method with reflection on the real printer instance
        printMethod.invoke(printer, value);
        // No exception means success
    }

}