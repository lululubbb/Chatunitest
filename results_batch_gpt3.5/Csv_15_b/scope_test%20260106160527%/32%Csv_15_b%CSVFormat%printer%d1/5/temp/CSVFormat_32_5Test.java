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

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_32_5Test {

    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        // Save the original System.out
        originalOut = System.out;
    }

    @AfterEach
    void tearDown() {
        // Restore the original System.out
        System.setOut(originalOut);
    }

    @Test
    @Timeout(8000)
    void testPrinterReturnsNonNullCSVPrinter() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = format.printer();
        assertNotNull(printer, "printer() should not return null");
        assertEquals(CSVPrinter.class, printer.getClass());

        // Verify that the underlying Appendable is System.out by reflection
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object out = outField.get(printer);
        assertSame(System.out, out, "CSVPrinter's underlying Appendable should be System.out");
    }

    @Test
    @Timeout(8000)
    void testPrinterWithCustomFormat() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVFormat customFormat = CSVFormat.DEFAULT.withDelimiter(';').withQuote('\'');
        CSVPrinter printer = customFormat.printer();
        assertNotNull(printer);
        assertEquals(CSVPrinter.class, printer.getClass());

        // Verify that the underlying Appendable is System.out by reflection
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object out = outField.get(printer);
        assertSame(System.out, out, "CSVPrinter's underlying Appendable should be System.out");
    }
}