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
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

class CSVPrinter_69_3Test {

    @Mock
    private Appendable out;

    @Mock
    private CSVFormat format;

    private CSVPrinter printer;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);
        printer = new CSVPrinter(out, format);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArray() throws IOException, Exception {
        Object[] record1 = new Object[] { "a", "b" };
        Object[] record2 = new Object[] { 1, 2 };
        Iterable<Object[]> records = Arrays.asList(record1, record2);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(records);

        Method printRecordVarargs = CSVPrinter.class.getMethod("printRecord", Object[].class);

        verify(spyPrinter, times(1)).printRecord((Object[]) record1);
        verify(spyPrinter, times(1)).printRecord((Object[]) record2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterable() throws IOException {
        Iterable<String> record1 = Arrays.asList("x", "y");
        Iterable<String> record2 = Collections.singletonList("z");
        Iterable<Iterable<String>> records = Arrays.asList(record1, record2);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(records);

        verify(spyPrinter, times(1)).printRecord((Iterable<?>) record1);
        verify(spyPrinter, times(1)).printRecord((Iterable<?>) record2);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withSingleObjects() throws IOException {
        Iterable<Object> records = Arrays.asList("single1", 123, 45.6);

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(records);

        verify(spyPrinter, times(1)).printRecord("single1");
        verify(spyPrinter, times(1)).printRecord(123);
        verify(spyPrinter, times(1)).printRecord(45.6);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_emptyIterable() throws IOException, Exception {
        Iterable<Object> empty = Collections.emptyList();

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(empty);

        // To avoid ambiguity, specify the method to verify using reflection
        Method printRecordIterable = CSVPrinter.class.getMethod("printRecord", Iterable.class);
        Method printRecordVarargs = CSVPrinter.class.getMethod("printRecord", Object[].class);

        // Verify no calls to printRecord(Iterable)
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        // Verify no calls to printRecord(Object...)
        verify(spyPrinter, never()).printRecord((Object[]) any());

        // To verify no calls to printRecord(Object), use the specific method via reflection and verify zero interactions
        // Since printRecord(Object) conflicts with printRecord(Object...), we verify no calls by checking invocation count on spy
        verify(spyPrinter, times(0)).printRecord((Object) any());
    }

}