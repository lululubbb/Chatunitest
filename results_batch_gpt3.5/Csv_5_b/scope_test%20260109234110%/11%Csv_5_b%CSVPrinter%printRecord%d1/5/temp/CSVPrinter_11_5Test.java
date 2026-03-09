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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_11_5Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        StringBuilder appendable = (StringBuilder) out;
        printer.printRecord("value1", 123, null, true);

        String output = appendable.toString();
        // Since print(Object) and println() are called, output should contain all values printed and a line break.
        // We do not know exact output formatting, but output should not be empty.
        assertFalse(output.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNoValues() throws IOException {
        StringBuilder appendable = (StringBuilder) out;
        printer.printRecord();

        String output = appendable.toString();
        // println() is still called, so output should contain at least a line break.
        assertFalse(output.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintForEachValue() throws Exception {
        CSVPrinter spyPrinter = Mockito.spy(new CSVPrinter(out, format));
        Object[] values = {"a", "b", "c"};

        spyPrinter.printRecord(values);

        for (Object value : values) {
            verify(spyPrinter).print(value);
        }
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_reflectionInvoke() throws Exception {
        Object[] values = {"x", "y"};
        Method method = CSVPrinter.class.getDeclaredMethod("printRecord", Object[].class);
        method.setAccessible(true);
        // Pass the Object[] as a single argument (cast to Object) to avoid varargs ambiguity
        method.invoke(printer, (Object) values);

        String output = out.toString();
        assertFalse(output.isEmpty());
    }
}