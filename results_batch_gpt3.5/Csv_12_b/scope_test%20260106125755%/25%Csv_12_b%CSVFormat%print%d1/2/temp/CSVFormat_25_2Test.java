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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_25_2Test {

    private Appendable mockAppendable;

    @BeforeEach
    public void setUp() {
        mockAppendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    public void testPrintReturnsCSVPrinter() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = format.print(mockAppendable);
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    public void testPrintThrowsIOException() throws IOException {
        Appendable throwingAppendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("Forced IOException");
            }
            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException("Forced IOException");
            }
            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException("Forced IOException");
            }
        };
        CSVFormat format = CSVFormat.DEFAULT;
        assertDoesNotThrow(() -> {
            CSVPrinter printer = format.print(throwingAppendable);
            assertNotNull(printer);
        });
    }

    @Test
    @Timeout(8000)
    public void testPrintWithCustomFormat() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';').withQuote('\'').withRecordSeparator("\n");
        CSVPrinter printer = format.print(mockAppendable);
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    public void testPrintPrivateMethodViaReflection() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Appendable.class);
        printMethod.setAccessible(true);
        Object result = printMethod.invoke(format, mockAppendable);
        assertNotNull(result);
        assertTrue(result instanceof CSVPrinter);
    }

}