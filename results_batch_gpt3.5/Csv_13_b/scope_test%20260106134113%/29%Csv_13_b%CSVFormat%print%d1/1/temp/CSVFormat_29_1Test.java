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

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testPrintReturnsCSVPrinter() throws IOException {
        Appendable appendable = new StringBuilder();
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);
        assertEquals(appendable, getAppendableFromPrinter(printer));
    }

    @Test
    @Timeout(8000)
    public void testPrintWithMockAppendable() throws IOException {
        Appendable mockAppendable = mock(Appendable.class);
        CSVPrinter printer = csvFormat.print(mockAppendable);
        assertNotNull(printer);
        assertEquals(mockAppendable, getAppendableFromPrinter(printer));
    }

    @Test
    @Timeout(8000)
    public void testPrintThrowsIOException() {
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

        assertThrows(IOException.class, () -> {
            CSVPrinter printer = csvFormat.print(throwingAppendable);
            // To trigger writing and cause IOException, call printer.print or printer.flush
            printer.print("test");
            printer.flush();
        });
    }

    private Appendable getAppendableFromPrinter(CSVPrinter printer) {
        try {
            Field outField = CSVPrinter.class.getDeclaredField("out");
            outField.setAccessible(true);
            return (Appendable) outField.get(printer);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Unable to access Appendable from CSVPrinter");
            return null;
        }
    }
}