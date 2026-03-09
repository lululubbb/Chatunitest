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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVPrinter_6_2Test {

    private CSVPrinter printer;
    private StringBuilder out;
    private CSVFormat format;

    @BeforeEach
    public void setUp() {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
        when(format.getDelimiter()).thenReturn(',');
        when(format.getEscape()).thenReturn('\\');
        printer = new CSVPrinter(out, format);
    }

    private void invokePrintAndEscape(CharSequence value, int offset, int len) throws Throwable {
        Method method = CSVPrinter.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class);
        method.setAccessible(true);
        try {
            method.invoke(printer, value, offset, len);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_NoSpecialChars() throws Throwable {
        String input = "simpletext";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("simpletext", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithDelimiter() throws Throwable {
        String input = "hello,world";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("hello\\,world", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithEscapeChar() throws Throwable {
        String input = "escape\\char";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("escape\\\\char", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithCR() throws Throwable {
        String input = "line\rbreak";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("line\\rbreak", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_WithLF() throws Throwable {
        String input = "line\nbreak";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("line\\nbreak", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_EmptyString() throws Throwable {
        String input = "";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_OffsetAndLength() throws Throwable {
        String input = "123,456\n789";
        // substring "456\n"
        invokePrintAndEscape(input, 4, 4);
        // 4:'4',5:'5',6:'6',7:'\n'
        assertEquals("456\\n", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscape_AllSpecialChars() throws Throwable {
        String input = "\r\n,\\";
        invokePrintAndEscape(input, 0, input.length());
        assertEquals("\\r\\n\\,\\\\", out.toString());
    }
}