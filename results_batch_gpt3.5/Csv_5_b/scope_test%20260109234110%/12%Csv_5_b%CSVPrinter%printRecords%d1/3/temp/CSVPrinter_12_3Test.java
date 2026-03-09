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
import org.mockito.Mockito;

class CSVPrinter_12_3Test {

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
    void testPrintRecords_withIterableContainingObjectArray() throws IOException {
        Object[] record = new Object[] { "a", "b" };
        Iterable<Object> iterable = Collections.singletonList(record);

        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);
        spyPrinter.printRecords(iterable);

        verify(spyPrinter).printRecord(record);
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingIterable() throws IOException {
        Iterable<String> innerIterable = Arrays.asList("a", "b");
        Iterable<Iterable<?>> iterable = Collections.singletonList(innerIterable);

        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);
        spyPrinter.printRecords(iterable);

        verify(spyPrinter).printRecord(innerIterable);
        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingOtherObject() throws IOException {
        Iterable<String> iterable = Collections.singletonList("value");

        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);
        spyPrinter.printRecords(iterable);

        verify(spyPrinter).printRecord("value");
        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        Iterable<Object> iterable = Collections.emptyList();

        csvPrinter.printRecords(iterable);

        // No exception and no calls to printRecord expected
        // Cannot verify private calls but no error means pass
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_privateMethodInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method method = CSVPrinter.class.getDeclaredMethod("printRecords", Iterable.class);
        method.setAccessible(true);

        Object[] array1 = new Object[] { "x", "y" };
        Iterable<Object> iterable = Collections.singletonList(array1);

        CSVPrinter spyPrinter = Mockito.spy(csvPrinter);
        method.invoke(spyPrinter, iterable);

        verify(spyPrinter).printRecord(array1);
    }
}