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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.Matcher;

public class CSVPrinter_13_3Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() throws IOException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    // Custom matcher to replace lambda argThat usage causing compile error
    private static Matcher<Object> notInstanceOfObjectArray() {
        return new TypeSafeMatcher<Object>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("not instance of Object[]");
            }

            @Override
            protected boolean matchesSafely(Object item) {
                return !(item instanceof Object[]);
            }
        };
    }

    private static Matcher<Object> instanceOfIterable() {
        return new TypeSafeMatcher<Object>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("instance of Iterable");
            }

            @Override
            protected boolean matchesSafely(Object item) {
                return item instanceof Iterable<?>;
            }
        };
    }

    @Test
    @Timeout(8000)
    public void testPrintRecords_withObjectArrayContainingObjectArray() throws Exception {
        Object[] innerArray = new Object[] { "a", "b" };
        Object[] values = new Object[] { innerArray };

        // Spy on printer to verify printRecord calls
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(innerArray);
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
        verify(spyPrinter, never()).printRecord((Object) argThat(notInstanceOfObjectArray()));
    }

    @Test
    @Timeout(8000)
    public void testPrintRecords_withObjectArrayContainingIterable() throws Exception {
        Iterable<String> iterable = Arrays.asList("x", "y");
        Object[] values = new Object[] { iterable };

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(iterable);
        verify(spyPrinter, never()).printRecord((Object[]) any(Object[].class));
        verify(spyPrinter, never()).printRecord((Object) argThat(instanceOfIterable()));
    }

    @Test
    @Timeout(8000)
    public void testPrintRecords_withObjectArrayContainingOtherObject() throws Exception {
        Object value = "test";
        Object[] values = new Object[] { value };

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord(value);
        verify(spyPrinter, never()).printRecord((Object[]) any(Object[].class));
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
    }

    @Test
    @Timeout(8000)
    public void testPrintRecords_withEmptyArray() throws IOException {
        Object[] values = new Object[0];

        printer.printRecords(values);

        // No exception and no output expected
        verify(out, never()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    public void testPrintRecords_withNullValueInArray() throws Exception {
        Object[] values = new Object[] { null };

        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecords(values);

        verify(spyPrinter).printRecord((Object) null);
        verify(spyPrinter, never()).printRecord((Object[]) any(Object[].class));
        verify(spyPrinter, never()).printRecord((Iterable<?>) any());
    }

    @Test
    @Timeout(8000)
    public void testPrivatePrintMethodInvocation() throws Exception {
        // Use reflection to invoke private print method to ensure coverage
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class);
        printMethod.setAccessible(true);

        CharSequence cs = "hello world";
        printMethod.invoke(printer, "obj", cs, 0, cs.length());

        // Verify that Appendable.append was called at least once
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    public void testPrivatePrintAndEscapeMethodInvocation() throws Exception {
        Method printAndEscapeMethod = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        printAndEscapeMethod.setAccessible(true);

        CharSequence cs = "escape,test";
        printAndEscapeMethod.invoke(printer, cs, 0, cs.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    public void testPrivatePrintAndQuoteMethodInvocation() throws Exception {
        Method printAndQuoteMethod = CSVPrinter.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class);
        printAndQuoteMethod.setAccessible(true);

        CharSequence cs = "quote,test";
        printAndQuoteMethod.invoke(printer, "obj", cs, 0, cs.length());

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

}