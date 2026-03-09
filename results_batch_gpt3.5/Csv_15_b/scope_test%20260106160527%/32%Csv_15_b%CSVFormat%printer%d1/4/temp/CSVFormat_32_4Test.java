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
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_32_4Test {

    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        // Save original System.out to restore later
        originalOut = System.out;
    }

    @AfterEach
    void tearDown() {
        // Restore original System.out
        System.setOut(originalOut);
    }

    @Test
    @Timeout(8000)
    void testPrinter_returnsCSVPrinter() throws Exception {
        // Redirect System.out to a mock PrintStream to verify usage
        PrintStream mockOut = mock(PrintStream.class);
        System.setOut(mockOut);

        CSVFormat format = CSVFormat.DEFAULT;

        CSVPrinter printer = format.printer();

        assertNotNull(printer);

        // Using reflection to check the CSVPrinter's 'out' field is System.out (mockOut)
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Appendable out = (Appendable) outField.get(printer);

        assertNotNull(out);

        // The 'out' field is a BufferedWriter wrapping the PrintStreamWriter wrapping System.out
        // Use reflection to get the underlying PrintStream inside the chain

        Object current = out;
        boolean foundMockOut = false;
        for (int i = 0; i < 5; i++) { // limit depth to avoid infinite loops
            if (current == null) break;
            Class<?> clazz = current.getClass();

            if (clazz.getName().equals("java.io.BufferedWriter")) {
                Field outFieldBW = clazz.getDeclaredField("out");
                outFieldBW.setAccessible(true);
                current = outFieldBW.get(current);
                continue;
            }

            if (clazz.getName().equals("java.io.PrintStreamWriter")) {
                Field outFieldPSW = clazz.getDeclaredField("out");
                outFieldPSW.setAccessible(true);
                current = outFieldPSW.get(current);
                continue;
            }

            if (current instanceof PrintStream) {
                foundMockOut = (current == mockOut);
                break;
            }

            break;
        }

        assertTrue(foundMockOut, "The underlying PrintStream should be the mocked System.out");

        // Also check that printer's format is the CSVFormat instance
        Method getFormat = CSVPrinter.class.getDeclaredMethod("getFormat");
        getFormat.setAccessible(true);
        CSVFormat usedFormat = (CSVFormat) getFormat.invoke(printer);
        assertSame(format, usedFormat);
    }

    @Test
    @Timeout(8000)
    void testPrinter_multipleCalls_returnDistinctPrinters() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer1 = format.printer();
        CSVPrinter printer2 = format.printer();

        assertNotNull(printer1);
        assertNotNull(printer2);
        assertNotSame(printer1, printer2);
    }
}