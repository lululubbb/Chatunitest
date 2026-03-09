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
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_67_6Test {

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
    void testPrintRecord_withIterable_callsPrintForEachValueAndPrintln() throws IOException {
        Iterable<Object> values = Arrays.asList("one", 2, null);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord(values);

        verify(spyPrinter, times(1)).print("one");
        verify(spyPrinter, times(1)).print(2);
        verify(spyPrinter, times(1)).print((Object) null);
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyIterable_callsPrintlnOnly() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord(values);

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_internalPrivatePrintAndPrintAndQuote() throws Exception {
        // Using reflection to invoke private print method with 4 params
        Method privatePrintMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        privatePrintMethod.setAccessible(true);

        // We invoke with a sample object and CharSequence
        CharSequence cs = "sampleValue";
        privatePrintMethod.invoke(printer, "obj", cs, 0, cs.length());

        // Using reflection to invoke private printAndEscape
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);
        printAndEscapeMethod.invoke(printer, cs, 0, cs.length());

        // Using reflection to invoke private printAndQuote
        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);
        printAndQuoteMethod.invoke(printer, "obj", cs, 0, cs.length());
    }
}