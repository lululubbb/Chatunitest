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
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVPrinter_69_1Test {

    @Mock
    private Appendable out;
    @Mock
    private CSVFormat format;

    private CSVPrinter printer;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        printer = new CSVPrinter(out, format);
    }
    
    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingObjectArray() throws IOException {
        Object[] record1 = new Object[] {"a", "b"};
        Iterable<Object[]> iterable = Collections.singletonList(record1);

        CSVPrinter spyPrinter = spy(printer);
        // disambiguate overloaded methods by casting to the exact method signature
        doNothing().when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).printRecord(anyIterable());

        spyPrinter.printRecords(iterable);

        verify(spyPrinter, times(1)).printRecord(record1);
        verify(spyPrinter, never()).printRecord(anyIterable());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingIterable() throws IOException {
        Iterable<String> innerIterable = Arrays.asList("x", "y");
        Iterable<Iterable<String>> iterable = Collections.singletonList(innerIterable);

        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).printRecord(anyIterable());

        spyPrinter.printRecords(iterable);

        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, times(1)).printRecord(innerIterable);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingOtherObject() throws IOException {
        Iterable<Object> iterable = Collections.singletonList("singleValue");

        CSVPrinter spyPrinter = spy(printer);
        doNothing().when(spyPrinter).printRecord((Object[]) any());
        doNothing().when(spyPrinter).printRecord(anyIterable());
        // disambiguate the overloaded printRecord(Object...) and printRecord(Iterable<?>) by casting to Object.class
        doNothing().when(spyPrinter).printRecord((Object) any());

        spyPrinter.printRecords(iterable);

        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord(anyIterable());
        verify(spyPrinter, times(1)).printRecord("singleValue");
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        Iterable<Object> iterable = Collections.emptyList();

        CSVPrinter spyPrinter = spy(printer);
        spyPrinter.printRecords(iterable);

        // No calls to printRecord expected
        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord(anyIterable());
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_reflectionInvocation() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Iterable<Object> iterable = Collections.singletonList("value");

        Method method = CSVPrinter.class.getDeclaredMethod("printRecords", Iterable.class);
        method.setAccessible(true);

        CSVPrinter spyPrinter = spy(printer);
        // disambiguate overloaded printRecord(Object...) and printRecord(Iterable<?>)
        doNothing().when(spyPrinter).printRecord((Object) any());

        method.invoke(spyPrinter, iterable);

        verify(spyPrinter, times(1)).printRecord("value");
    }
}