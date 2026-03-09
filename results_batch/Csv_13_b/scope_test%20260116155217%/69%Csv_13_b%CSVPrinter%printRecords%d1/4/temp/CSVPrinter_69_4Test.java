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
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_69_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        // Use a real CSVFormat instance instead of mock to avoid issues
        format = CSVFormat.DEFAULT;
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjects() throws IOException {
        Iterable<?> iterable = Arrays.asList("one", "two", "three");
        printer.printRecords(iterable);
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjectArrays() throws IOException {
        Object[] arr1 = new Object[] { "a", "b" };
        Object[] arr2 = new Object[] { "c", "d" };
        Iterable<?> iterable = Arrays.asList((Object) arr1, (Object) arr2);
        printer.printRecords(iterable);
        verify(out, atLeast(2)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfIterables() throws IOException {
        Iterable<String> inner1 = Arrays.asList("x", "y");
        Iterable<String> inner2 = Arrays.asList("z");
        Iterable<?> iterable = Arrays.asList((Object) inner1, (Object) inner2);
        printer.printRecords(iterable);
        verify(out, atLeast(2)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        Iterable<?> iterable = Collections.emptyList();
        printer.printRecords(iterable);
        verify(out, never()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withMixedIterable() throws IOException {
        Object[] objArr = new Object[] { "v1", "v2" };
        Iterable<String> iterable = Arrays.asList("a", "b");
        Object singleObject = "single";

        Iterable<?> mixed = Arrays.asList((Object) objArr, (Object) iterable, singleObject);
        printer.printRecords(mixed);
        verify(out, atLeast(3)).append(any(CharSequence.class));
    }
}