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
import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_32_2Test {

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
    void testPrinterReturnsCSVPrinter() throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        CSVPrinter printer = csvFormat.printer();

        assertNotNull(printer, "CSVPrinter should not be null");
        // The CSVPrinter should write to System.out and use the given CSVFormat
        // We can verify the CSVFormat inside printer by reflection or by behavior
        // But since CSVPrinter is final and no getters, we just check non-null and class
        assertEquals(CSVPrinter.class, printer.getClass());
    }

    @Test
    @Timeout(8000)
    void testPrinterDoesNotThrowIOException() {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        assertDoesNotThrow(() -> {
            CSVPrinter printer = csvFormat.printer();
            assertNotNull(printer);
        });
    }

    @Test
    @Timeout(8000)
    void testPrinterWithMockedSystemOut() throws IOException {
        // Replace System.out with a mock to verify interaction if needed
        PrintStream mockPrintStream = mock(PrintStream.class);
        System.setOut(mockPrintStream);

        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVPrinter printer = csvFormat.printer();

        assertNotNull(printer);
        // We cannot verify internal usage of System.out in CSVPrinter since it's final, but no exceptions thrown is good
    }

    @Test
    @Timeout(8000)
    void testPrivatePrinterMethodViaReflection() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        Method printerMethod = CSVFormat.class.getDeclaredMethod("printer");
        printerMethod.setAccessible(true);
        Object result = printerMethod.invoke(csvFormat);

        assertNotNull(result);
        assertTrue(result instanceof CSVPrinter);
    }
}