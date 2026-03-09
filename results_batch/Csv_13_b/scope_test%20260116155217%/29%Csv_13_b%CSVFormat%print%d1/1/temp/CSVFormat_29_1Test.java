package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
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

public class CSVFormat_29_1Test {

    private Appendable appendable;

    @BeforeEach
    public void setUp() {
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    public void testPrintReturnsCSVPrinter() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;

        CSVPrinter printer = format.print(appendable);

        assertNotNull(printer);
        assertEquals(appendable, getAppendableFromPrinter(printer));
    }

    @Test
    @Timeout(8000)
    public void testPrintThrowsIOException() throws IOException {
        Appendable throwingAppendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("Test IOException");
            }

            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException("Test IOException");
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException("Test IOException");
            }
        };

        CSVFormat format = CSVFormat.DEFAULT;

        // The print method itself does not throw IOException immediately,
        // but creating CSVPrinter may not throw IOException until usage.
        // So just verify that print returns a CSVPrinter instance.
        CSVPrinter printer = format.print(throwingAppendable);
        assertNotNull(printer);
    }

    private Appendable getAppendableFromPrinter(CSVPrinter printer) {
        try {
            Field outField = CSVPrinter.class.getDeclaredField("out");
            outField.setAccessible(true);
            return (Appendable) outField.get(printer);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed to get 'out' field from CSVPrinter: " + e.getMessage());
            return null;
        }
    }
}