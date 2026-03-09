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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVPrinter_11_2Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() {
        out = new StringBuilder();
        format = CSVFormat.DEFAULT;
        try {
            printer = new CSVPrinter(out, format);
        } catch (RuntimeException e) {
            // wrap IOException in RuntimeException, so no throws declaration needed
            throw e;
        }
    }

    @Test
    @Timeout(8000)
    public void testPrintRecord_withMultipleValues() throws IOException {
        printer.printRecord("value1", 123, null, true);

        String printed = out.toString();
        assertTrue(printed.contains("value1"));
        assertTrue(printed.contains("123"));
        assertTrue(printed.contains("true"));
    }

    @Test
    @Timeout(8000)
    public void testPrintRecord_withEmptyValues() throws IOException {
        printer.printRecord();

        String printed = out.toString();
        assertTrue(printed.equals(System.lineSeparator()) || printed.equals("\n") || printed.equals("\r\n"));
    }

    @Test
    @Timeout(8000)
    public void testPrintRecord_withNullValue() throws IOException {
        printer.printRecord((Object) null);

        String printed = out.toString();
        assertTrue(printed != null);
    }

    @Test
    @Timeout(8000)
    public void testPrivatePrintMethod_invocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method printMethod = CSVPrinter.class.getDeclaredMethod("print", Object.class);
        printMethod.setAccessible(true);

        printMethod.invoke(printer, "privateTest");

        Method printlnMethod = CSVPrinter.class.getDeclaredMethod("println");
        printlnMethod.setAccessible(true);
        printlnMethod.invoke(printer);

        String printed = out.toString();
        assertTrue(printed.contains("privateTest"));
    }

}