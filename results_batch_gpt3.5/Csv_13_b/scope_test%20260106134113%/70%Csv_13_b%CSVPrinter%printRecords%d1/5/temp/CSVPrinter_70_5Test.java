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

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_70_5Test {

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
    void testPrintRecords_withSingleObjects() throws IOException {
        printer.printRecords("a", 1, 3.14);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withObjectArray() throws IOException {
        Object[] array1 = new Object[] {"x", "y"};
        Object[] array2 = new Object[] {1, 2, 3};

        // Cast to Object to ensure varargs call is correct
        printer.printRecords((Object) array1, (Object) array2);

        verify(out, atLeast(2)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterable() throws IOException {
        Iterable<String> iterable1 = Arrays.asList("foo", "bar");
        Iterable<Integer> iterable2 = Collections.singletonList(42);

        printer.printRecords((Object) iterable1, (Object) iterable2);

        verify(out, atLeast(2)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_mixedArguments() throws IOException {
        Object[] array = new Object[] {"a", "b"};
        Iterable<String> iterable = Arrays.asList("c", "d");
        String single = "e";

        printer.printRecords((Object) array, (Object) iterable, single);

        verify(out, atLeast(3)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmpty() throws IOException {
        printer.printRecords();

        verify(out, never()).append(any(CharSequence.class));
    }
}