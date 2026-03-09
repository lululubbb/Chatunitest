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

class CSVPrinter_68_3Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setup() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues_callsPrintAndPrintln() throws IOException {
        Object val1 = "value1";
        Object val2 = 123;
        Object val3 = null;

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecord(val1, val2, val3);

        verify(spyPrinter, times(1)).print(val1);
        verify(spyPrinter, times(1)).print(val2);
        verify(spyPrinter, times(1)).print(val3);
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNoValues_onlyCallsPrintln() throws IOException {
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecord();

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_privatePrintMethodInvocation() throws Exception {
        Method printRecordMethod = CSVPrinter.class.getDeclaredMethod("printRecord", Object[].class);
        printRecordMethod.setAccessible(true);

        Object[] params = new Object[] { "a", "b" };

        CSVPrinter spyPrinter = spy(printer);

        // When invoking varargs method via reflection, wrap params in Object[] as single argument
        printRecordMethod.invoke(spyPrinter, new Object[] { params });

        verify(spyPrinter).print("a");
        verify(spyPrinter).print("b");
        verify(spyPrinter).println();
    }
}