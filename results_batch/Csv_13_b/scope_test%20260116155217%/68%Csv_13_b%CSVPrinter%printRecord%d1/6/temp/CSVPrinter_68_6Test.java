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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_68_6Test {

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
        CSVPrinter spyPrinter = Mockito.spy(printer);

        Object val1 = "value1";
        Object val2 = 123;
        Object val3 = null;

        spyPrinter.printRecord(val1, val2, val3);

        verify(spyPrinter, times(1)).print(val1);
        verify(spyPrinter, times(1)).print(val2);
        verify(spyPrinter, times(1)).print(val3);
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_noValues() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord();

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_reflection_invokesPrivatePrint() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object val = "test";

        Method privatePrint = CSVPrinter.class.getDeclaredMethod("print", Object.class);
        privatePrint.setAccessible(true);

        // invoke private print on the real printer instance
        privatePrint.invoke(printer, val);

        // create spy AFTER invoking private method to capture calls on spy
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord(val);

        // Use doCallRealMethod() to ensure spy uses real methods for print and println
        doCallRealMethod().when(spyPrinter).print(val);
        doCallRealMethod().when(spyPrinter).println();

        verify(spyPrinter, times(1)).print(val);
        verify(spyPrinter, times(1)).println();
    }
}