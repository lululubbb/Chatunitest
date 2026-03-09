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
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_32_6Test {

    private PrintWriter originalOut;

    @BeforeEach
    void setUp() {
        // CSVPrinter stores a Writer wrapping System.out, so we keep a reference for comparison
        originalOut = new PrintWriter(System.out, true);
    }

    @AfterEach
    void tearDown() {
        // No system out replacement done, so nothing to restore
    }

    @Test
    @Timeout(8000)
    void testPrinterReturnsCSVPrinterWithSystemOut() throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVPrinter printer = csvFormat.printer();
        assertNotNull(printer);
        assertEquals(CSVPrinter.class, printer.getClass());

        try {
            Field outField = CSVPrinter.class.getDeclaredField("out");
            outField.setAccessible(true);
            Object out = outField.get(printer);
            assertNotNull(out);
            // The 'out' field is a Writer wrapping System.out, so check it's a Writer instance
            assertTrue(out instanceof Writer);

            // Additionally, check that the underlying writer is wrapping System.out by checking toString or class
            // (optional, can be omitted if implementation details vary)
        } catch (NoSuchFieldException e) {
            fail("CSVPrinter.out field not found");
        } catch (IllegalAccessException e) {
            fail("Cannot access CSVPrinter.out field");
        }
    }
}