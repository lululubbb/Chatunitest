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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_10_6Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
        printer = spy(new CSVPrinter(out, format));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        Iterable<Object> values = Arrays.asList("value1", 123, null, "value4");

        printer.printRecord(values);

        // Verify print called for each value
        verify(printer, times(4)).print(any());
        // Verify println called once at the end
        verify(printer, times(1)).println();

        // The out should contain the printed values (string representations)
        String output = out.toString();
        assertTrue(output.contains("value1"));
        assertTrue(output.contains("123"));
        // null may be printed as "null" or empty string, so no strict check
        assertTrue(output.contains("value4"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyIterable() throws IOException {
        Iterable<Object> values = Collections.emptyList();

        printer.printRecord(values);

        verify(printer, never()).print(any());
        verify(printer, times(1)).println();

        String output = out.toString();
        assertNotNull(output);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withSingleValue() throws IOException {
        Iterable<Object> values = Collections.singletonList("singleValue");

        printer.printRecord(values);

        verify(printer, times(1)).print("singleValue");
        verify(printer, times(1)).println();

        String output = out.toString();
        assertTrue(output.contains("singleValue"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNullIterable() throws Throwable {
        // Use reflection to call printRecord(Iterable<?>) to avoid ambiguity
        Method printRecordMethod = CSVPrinter.class.getMethod("printRecord", Iterable.class);
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            try {
                printRecordMethod.invoke(printer, (Object) null);
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause != null) {
                    throw cause;
                } else {
                    throw e;
                }
            }
        });
        assertNotNull(thrown);
    }
}