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
import org.mockito.InOrder;

class CSVPrinter_13_1Test {

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
    void testPrintRecords_withMixedValues_callsCorrectPrintRecordMethods() throws IOException {
        Object[] values = new Object[] {
                new Object[] { "a", "b" },
                java.util.Arrays.asList("c", "d"),
                "singleValue"
        };

        CSVPrinter spyPrinter = spy(printer);

        // Use doCallRealMethod to allow spying on printRecord methods
        doCallRealMethod().when(spyPrinter).printRecord((Object[]) any());
        doCallRealMethod().when(spyPrinter).printRecord((Iterable<?>) any());

        // For printRecord(Object), disambiguate by using doAnswer with reflection
        try {
            Method printRecordSingle = CSVPrinter.class.getMethod("printRecord", Object.class);
            // If method exists, use it directly
            doCallRealMethod().when(spyPrinter).printRecord(any());
        } catch (NoSuchMethodException e) {
            // Method does not exist, so intercept calls to printRecord(Object) via doAnswer
            doAnswer(invocation -> {
                Object arg = invocation.getArgument(0);
                Method printRecordVarargs = CSVPrinter.class.getMethod("printRecord", Object[].class);
                return printRecordVarargs.invoke(spyPrinter, (Object) new Object[] { arg });
            }).when(spyPrinter).printRecord(any());
        }

        spyPrinter.printRecords(values);

        InOrder inOrder = inOrder(spyPrinter);
        inOrder.verify(spyPrinter).printRecord((Object[]) values[0]);
        inOrder.verify(spyPrinter).printRecord((Iterable<?>) values[1]);
        inOrder.verify(spyPrinter).printRecord(values[2]);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @Timeout(8000)
    void testPrintPrivatePrintObjectCharSequenceOffsetLen() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        String testString = "hello world";
        method.invoke(printer, testString, testString, 0, testString.length());
        // no exception means success
    }

    @Test
    @Timeout(8000)
    void testPrintPrivatePrintAndEscape() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        String testString = "escape,test";
        method.invoke(printer, testString, 0, testString.length());
        // no exception means success
    }

    @Test
    @Timeout(8000)
    void testPrintPrivatePrintAndQuote() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        String testString = "quote\"test";
        method.invoke(printer, testString, testString, 0, testString.length());
        // no exception means success
    }
}