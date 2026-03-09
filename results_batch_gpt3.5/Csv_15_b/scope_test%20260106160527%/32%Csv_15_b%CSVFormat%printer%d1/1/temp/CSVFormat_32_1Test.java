package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_32_1Test {

    private CSVFormat csvFormat;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.newFormat(',');
        // Save original System.out
        originalOut = System.out;
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    @Timeout(8000)
    void testPrinterNotNull() throws IOException {
        CSVPrinter printer = csvFormat.printer();
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    void testPrinterUsesSystemOut() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Create a spy on an OutputStream wrapping System.out to detect usage
        OutputStream originalOutputStream = getOutputStreamFromPrintStream(originalOut);
        OutputStream spyOut = spy(originalOutputStream);

        // Create a PrintStream that uses the spyOut OutputStream
        PrintStream spyPrintStream = new PrintStream(spyOut, true);
        System.setOut(spyPrintStream);

        CSVPrinter printer = csvFormat.printer();
        assertNotNull(printer);

        // Write to printer and verify that spyOut's write method was called
        printer.print("test");

        verify(spyOut, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
    }

    private OutputStream getOutputStreamFromPrintStream(PrintStream printStream) {
        try {
            // Try to get the private 'out' field of PrintStream (the underlying OutputStream)
            Field outField = PrintStream.class.getDeclaredField("out");
            outField.setAccessible(true);
            Object outObj = outField.get(printStream);
            if (outObj instanceof OutputStream) {
                return (OutputStream) outObj;
            }
        } catch (Exception e) {
            // ignore and fallback below
        }
        // Fallback: wrap original PrintStream in an OutputStream that delegates to it
        return new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                printStream.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                printStream.write(b, off, len);
            }
        };
    }
}