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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_31_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_withValidAppendable_returnsCSVPrinter() throws IOException {
        Appendable appendable = new StringBuilder();
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);
        assertEquals(appendable, getOut(printer));
    }

    @Test
    @Timeout(8000)
    void testPrint_withMockedAppendable_returnsCSVPrinter() throws IOException {
        Appendable appendable = mock(Appendable.class);
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);
        assertEquals(appendable, getOut(printer));
    }

    @Test
    @Timeout(8000)
    void testPrint_withAppendableThatThrowsIOException() {
        Appendable appendable = mock(Appendable.class);
        try {
            doThrow(new IOException("Test IO Exception")).when(appendable).append(any(CharSequence.class));
            // Creating CSVPrinter does not trigger append, so no IOException expected here
            CSVPrinter printer = csvFormat.print(appendable);
            assertNotNull(printer);
        } catch (IOException e) {
            fail("print method should not throw IOException during CSVPrinter creation");
        }
    }

    private Appendable getOut(CSVPrinter printer) {
        try {
            Field outField = CSVPrinter.class.getDeclaredField("out");
            outField.setAccessible(true);
            return (Appendable) outField.get(printer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}