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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_ReturnsCSVPrinter() throws IOException, NoSuchFieldException, IllegalAccessException {
        Appendable appendable = new StringBuilder();
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);

        // Use reflection to get the private field 'out' from CSVPrinter
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object outValue = outField.get(printer);

        assertSame(appendable, outValue);
    }

    @Test
    @Timeout(8000)
    void testPrint_WithMockAppendable() throws IOException, NoSuchFieldException, IllegalAccessException {
        Appendable mockAppendable = mock(Appendable.class);
        CSVPrinter printer = csvFormat.print(mockAppendable);
        assertNotNull(printer);

        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object outValue = outField.get(printer);

        assertSame(mockAppendable, outValue);
    }

    @Test
    @Timeout(8000)
    void testPrint_ThrowsIOException() throws IOException, NoSuchFieldException, IllegalAccessException {
        Appendable throwingAppendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("append error");
            }

            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException("append error");
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException("append error");
            }
        };
        CSVPrinter printer = csvFormat.print(throwingAppendable);
        assertNotNull(printer);

        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object outValue = outField.get(printer);

        assertSame(throwingAppendable, outValue);
    }

    @Test
    @Timeout(8000)
    void testPrint_WithDifferentCSVFormat() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVFormat customFormat = CSVFormat.EXCEL.withDelimiter(';').withQuote('\'');
        Appendable appendable = new StringBuilder();
        CSVPrinter printer = customFormat.print(appendable);
        assertNotNull(printer);

        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object outValue = outField.get(printer);

        assertSame(appendable, outValue);
    }
}