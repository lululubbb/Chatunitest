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
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_69_5Test {

    private CSVPrinter printer;
    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingObjectArray() throws Exception {
        Object[] array = new Object[] { "a", "b" };
        Iterable<Object> iterable = Collections.singletonList(array);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(iterable);

        verify(spyPrinter).printRecord(array);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingIterable() throws Exception {
        Iterable<String> innerIterable = Arrays.asList("x", "y");
        Iterable<Object> iterable = Collections.singletonList(innerIterable);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(iterable);

        verify(spyPrinter).printRecord(innerIterable);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withIterableContainingSingleObject() throws Exception {
        String singleValue = "single";
        Iterable<Object> iterable = Collections.singletonList(singleValue);

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(iterable);

        verify(spyPrinter).printRecord(singleValue);
    }

    @Test
    @Timeout(8000)
    void testPrintRecords_withEmptyIterable() throws IOException {
        Iterable<Object> emptyIterable = Collections.emptyList();

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.printRecords(emptyIterable);

        try {
            Method methodVarargs = CSVPrinter.class.getMethod("printRecord", Object[].class);
            Method methodIterable = CSVPrinter.class.getMethod("printRecord", Iterable.class);

            // verify that printRecord(Object[]) was never called
            verify(spyPrinter, never()).printRecord((Object[]) any());

            // verify that printRecord(Iterable<?>) was never called
            verify(spyPrinter, never()).printRecord((Iterable<?>) any());

            // verify that printRecord(Object) was never called
            // Using reflection to invoke verify to avoid ambiguity
            Method verifyMethod = Mockito.class.getMethod("verify", Object.class, org.mockito.verification.VerificationMode.class);
            Object verification = verifyMethod.invoke(null, spyPrinter, never());

            Method printRecordObject = CSVPrinter.class.getMethod("printRecord", Object.class);
            printRecordObject.invoke(verification, (Object) any());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}