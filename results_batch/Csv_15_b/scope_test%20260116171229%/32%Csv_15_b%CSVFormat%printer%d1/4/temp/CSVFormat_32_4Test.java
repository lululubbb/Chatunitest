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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_32_4Test {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    void setUp() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @Timeout(8000)
    void testPrinter_DefaultCSVFormat_PrinterNotNull() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        try (CSVPrinter printer = format.print(new PrintStream(testOut))) {
            assertNotNull(printer);
            printer.print("test");
            printer.flush();
        }
        String output = testOut.toString();
        assertTrue(output.contains("test"));
    }

    @Test
    @Timeout(8000)
    void testPrinter_ExcelCSVFormat_PrinterNotNull() throws IOException {
        CSVFormat format = CSVFormat.EXCEL;
        try (CSVPrinter printer = format.print(new PrintStream(testOut))) {
            assertNotNull(printer);
            printer.print("excel");
            printer.flush();
        }
        String output = testOut.toString();
        assertTrue(output.contains("excel"));
    }

    @Test
    @Timeout(8000)
    void testPrinter_CustomCSVFormat_PrinterNotNull() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';').withQuote('\'');
        try (CSVPrinter printer = format.print(new PrintStream(testOut))) {
            assertNotNull(printer);
            printer.print("custom");
            printer.flush();
        }
        String output = testOut.toString();
        assertTrue(output.contains("custom"));
    }
}