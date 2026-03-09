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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_67_6Test {

    private CSVPrinter csvPrinter;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        out = Mockito.mock(Appendable.class);
        format = Mockito.mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withIterable_callsPrintForEachValueAndPrintln() throws IOException {
        Iterable<Object> values = Arrays.asList("val1", 123, null);

        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);

        spyPrinter.printRecord(values);

        for (Object val : values) {
            verify(spyPrinter).print(val);
        }
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyIterable_onlyCallsPrintln() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);

        spyPrinter.printRecord(values);

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrivatePrintAndEscapeAndPrintAndQuote() throws Exception {
        // Use reflection to invoke private print methods to cover them indirectly
        Method printPrivate = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printPrivate.setAccessible(true);

        Method printAndEscape = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscape.setAccessible(true);

        Method printAndQuote = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuote.setAccessible(true);

        CharSequence cs = "testValue";
        printPrivate.invoke(csvPrinter, null, cs, 0, cs.length());
        printPrivate.invoke(csvPrinter, "obj", cs, 0, cs.length());

        printAndEscape.invoke(csvPrinter, cs, 0, cs.length());
        printAndQuote.invoke(csvPrinter, "obj", cs, 0, cs.length());
    }
}