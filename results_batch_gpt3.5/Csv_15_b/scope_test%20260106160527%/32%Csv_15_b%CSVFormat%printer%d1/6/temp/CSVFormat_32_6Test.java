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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_32_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrinterReturnsCSVPrinter() throws IOException {
        CSVPrinter printer = csvFormat.printer();
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    void testPrinterWritesToSystemOut() throws Exception {
        CSVPrinter printer = csvFormat.printer();

        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object outObject = outField.get(printer);

        assertTrue(outObject instanceof Writer);

        // The Writer is an OutputStreamWriter wrapping System.out.
        // So via reflection, get the "lock" field of OutputStreamWriter,
        // which is the underlying OutputStream (System.out).

        Field lockField = OutputStreamWriter.class.getDeclaredField("lock");
        lockField.setAccessible(true);
        Object underlyingOut = lockField.get(outObject);

        // The underlyingOut may be a PrintStream wrapping System.out,
        // so we check if it is System.out or a wrapper around it.
        assertSame(System.out, underlyingOut);
    }
}