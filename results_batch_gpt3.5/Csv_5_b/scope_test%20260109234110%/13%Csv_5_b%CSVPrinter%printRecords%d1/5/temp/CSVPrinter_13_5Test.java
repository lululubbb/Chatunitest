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

class CSVPrinter_13_5Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter csvPrinter;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withVariousObjects() throws IOException {
        Object[] values = new Object[] {
            new Object[] {"a", "b"},
            java.util.Arrays.asList("c", "d"),
            "e"
        };

        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);

        spyPrinter.printRecords(values);

        verify(spyPrinter, times(1)).printRecord((Object[]) values[0]);
        verify(spyPrinter, times(1)).printRecord((Iterable<?>) values[1]);
        verify(spyPrinter, times(1)).printRecord(values[2]);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_emptyArray() throws IOException {
        Object[] values = new Object[0];

        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);

        spyPrinter.printRecords(values);

        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_nullElements() throws IOException {
        Object[] values = new Object[] { null, new Object[] {null}, java.util.Arrays.asList((Object) null) };

        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);

        spyPrinter.printRecords(values);

        verify(spyPrinter, times(1)).printRecord((Object[]) values[1]);
        verify(spyPrinter, times(1)).printRecord((Iterable<?>) values[2]);
        verify(spyPrinter, times(1)).printRecord((Object) null);
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndEscapeMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        String testString = "escape,test";
        method.invoke(csvPrinter, testString, 0, testString.length());

        verify(out, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintAndQuoteMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        String testString = "quote,test";
        method.invoke(csvPrinter, testString, testString, 0, testString.length());

        verify(out, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrivatePrintMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        String testString = "print,test";
        method.invoke(csvPrinter, testString, testString, 0, testString.length());

        verify(out, atLeastOnce()).append(anyChar());
    }
}