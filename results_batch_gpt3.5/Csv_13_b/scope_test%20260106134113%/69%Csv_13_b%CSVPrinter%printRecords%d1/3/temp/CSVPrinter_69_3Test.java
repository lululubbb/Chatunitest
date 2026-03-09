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
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_69_3Test {

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
    void testPrintRecords_withIterableOfObjects() throws IOException {
        List<String> values = Arrays.asList("a", "b", "c");

        printer.printRecords(values);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableOfObjectArrays() throws IOException {
        List<Object[]> values = Arrays.asList(new Object[]{"a", "b"}, new Object[]{"c", "d"});

        printer.printRecords(values);

        verify(out, atLeast(4)).append(any(CharSequence.class)); // at least 4 appends for 4 elements
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingIterable() throws IOException {
        List<Iterable<?>> values = Arrays.asList(
                Arrays.asList("a", "b"),
                Arrays.asList("c", "d")
        );

        printer.printRecords(values);

        verify(out, atLeast(4)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingMixedTypes() throws IOException {
        List<Object> values = Arrays.asList(
                new Object[]{"x", "y"},
                Arrays.asList("a", "b"),
                "single"
        );

        printer.printRecords(values);

        verify(out, atLeast(5)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        List<Object> empty = Collections.emptyList();

        printer.printRecords(empty);

        verify(out, never()).append(any(CharSequence.class));
    }
}