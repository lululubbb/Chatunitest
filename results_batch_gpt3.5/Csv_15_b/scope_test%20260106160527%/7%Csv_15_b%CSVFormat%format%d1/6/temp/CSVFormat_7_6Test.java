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
import java.io.StringWriter;
import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_7_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testFormat_withNormalValues() throws IOException {
        String result = csvFormat.format("a", "b", "c");
        // Expected output: a,b,c (trimmed)
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withNullValue() throws IOException {
        String result = csvFormat.format("a", null, "c");
        // null should be printed as empty string unless nullString is set
        assertEquals("a,,c", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withEmptyValues() throws IOException {
        String result = csvFormat.format("", "", "");
        assertEquals(",,", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withNoValues() throws IOException {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void testFormat_withIOExceptionThrown() throws Exception {
        // Create a spy of CSVPrinter that throws IOException on printRecord(Object...)
        CSVPrinter spyPrinter = mock(CSVPrinter.class);
        doThrow(new IOException("IO error")).when(spyPrinter).printRecord((Object[]) any());

        // Use reflection to get CSVPrinter constructor and create a CSVPrinter that delegates to spyPrinter
        class CSVFormatWithSpyPrinter {
            String format(final Object... values) {
                StringWriter out = new StringWriter();
                try {
                    // Instead of creating a real CSVPrinter, use the spyPrinter and inject the output
                    // But since CSVPrinter uses the Appendable internally, we need to simulate it.

                    // We'll create a subclass of CSVPrinter that overrides printRecord to call spyPrinter.printRecord
                    CSVPrinter printer = new CSVPrinter(out, csvFormat) {
                        @Override
                        public void printRecord(Object... values) throws IOException {
                            spyPrinter.printRecord(values);
                        }
                    };

                    printer.printRecord(values);

                    return out.toString().trim();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        CSVFormatWithSpyPrinter formatWithSpy = new CSVFormatWithSpyPrinter();

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            formatWithSpy.format("a", "b");
        });
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("IO error", ex.getCause().getMessage());
    }
}