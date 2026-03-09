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

class CSVPrinter_12_1Test {

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
    void testPrintRecords_withIterableContainingObjectArray() throws IOException {
        Object[] record = new Object[] { "a", "b" };
        Iterable<Object> values = Collections.singletonList(record);

        printer.printRecords(values);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingIterable() throws IOException {
        Iterable<String> innerIterable = Arrays.asList("x", "y");
        Iterable<Object> values = Collections.singletonList(innerIterable);

        printer.printRecords(values);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingSingleObject() throws IOException {
        Iterable<Object> values = Collections.singletonList("singleValue");

        printer.printRecords(values);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        printer.printRecords(values);

        verify(out, never()).append(any(CharSequence.class));
    }

}