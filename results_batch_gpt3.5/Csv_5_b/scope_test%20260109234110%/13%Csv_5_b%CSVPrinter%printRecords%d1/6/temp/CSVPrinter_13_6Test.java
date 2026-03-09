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

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    public void testPrintRecords_withObjectArrayContainingObjectArray() throws Exception {
        Object[] nestedArray = new Object[] {"nested1", "nested2"};
        Object[] values = new Object[] {nestedArray};

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord((Object[]) nestedArray);
    }

    @Test
    @Timeout(8000)
    public void testPrintRecords_withObjectArrayContainingIterable() throws Exception {
        Iterable<String> iterable = Arrays.asList("a", "b");
        Object[] values = new Object[] {iterable};

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(iterable);
    }

    @Test
    @Timeout(8000)
    public void testPrintRecords_withObjectArrayContainingOtherObject() throws Exception {
        Object obj = "stringValue";
        Object[] values = new Object[] {obj};

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(obj);
    }

    @Test
    @Timeout(8000)
    public void testPrintRecords_withEmptyArray() throws IOException {
        Object[] values = new Object[0];

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter, never()).printRecord((Object[]) any());
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object) any());
    }

    @Test
    @Timeout(8000)
    public void testPrintRecords_withNullValueInArray() throws Exception {
        Object[] values = new Object[] {null};

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord((Object) isNull());
    }

    @Test
    @Timeout(8000)
    public void testInvokePrintAndEscapePrivateMethodUsingReflection() throws Exception {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, "abc", 0, 3);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    public void testInvokePrintAndQuotePrivateMethodUsingReflection() throws Exception {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, "obj", "quoted", 0, 6);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    public void testInvokePrintPrivateMethodUsingReflection() throws Exception {
        Method method = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        method.setAccessible(true);

        method.invoke(printer, "obj", "value", 0, 5);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }
}