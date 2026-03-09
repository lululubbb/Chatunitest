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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVPrinter_13_6Test {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    public void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    public void testPrintRecords_withObjectArrayContainingObjectArrayIterableAndObject() throws Exception {
        Object[] nestedArray = new Object[] { "nested1", "nested2" };
        Iterable<String> iterable = Arrays.asList("iter1", "iter2");
        Object obj = "stringObject";

        Object[] input = new Object[] { nestedArray, iterable, obj };

        // Spy on printer to verify calls to printRecord
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(input);

        verify(spyPrinter).printRecord((Object[]) nestedArray);
        verify(spyPrinter).printRecord(iterable);
        verify(spyPrinter).printRecord(obj);
    }

    @Test
    @Timeout(8000)
    public void testPrintRecords_withEmptyArray() throws IOException {
        Object[] input = new Object[0];
        printer.printRecords(input);
        // No exception and no output expected

        try {
            // verifyNoInteractions takes varargs, so pass the mock directly
            Method verifyNoInteractionsMethod = org.mockito.Mockito.class.getMethod("verifyNoInteractions", Object[].class);
            verifyNoInteractionsMethod.invoke(null, (Object) new Object[] { out });
        } catch (NoSuchMethodException e) {
            // fallback for Mockito versions without verifyNoInteractions
            verify(out, never()).append(any(CharSequence.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    public void testPrintRecords_withNullValueInArray() throws Exception {
        Object[] input = new Object[] { null };
        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(input);
        verify(spyPrinter).printRecord((Object) null);
    }

    @Test
    @Timeout(8000)
    public void testPrintRecords_invokesPrivatePrintRecord() throws Exception {
        // Use reflection to invoke private printRecord(Object...) method
        Method printRecordMethod = CSVPrinter.class.getDeclaredMethod("printRecord", Object[].class);
        printRecordMethod.setAccessible(true);

        Object[] record = new Object[] { "a", "b" };
        printRecordMethod.invoke(printer, (Object) record);

        // No exception means success, out.append may be called internally (not verified here)
    }

}