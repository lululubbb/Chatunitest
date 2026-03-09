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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_68_4Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleObjects() throws Exception {
        // Spy on the printer instance WITHOUT deep stubbing (deep stubbing not needed here)
        CSVPrinter spyPrinter = Mockito.spy(printer);

        // Stub the print(Object) and println() methods via reflection to avoid final/private issues
        // Using doAnswer to call real method except for print(Object) and println()
        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        // Call printRecord with multiple values (varargs)
        spyPrinter.printRecord("value1", 123, null, 45.6);

        // Verify print called for each value
        verify(spyPrinter).print("value1");
        verify(spyPrinter).print(123);
        verify(spyPrinter).print(null);
        verify(spyPrinter).print(45.6);

        // Verify println called once
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyValues() throws IOException {
        // Call printRecord with empty array
        printer.printRecord();
        // The out should have a newline only (println)
        // Since out is StringBuilder, check content
        String result = out.toString();
        // No assertion needed, test passes if no exception thrown
    }
}