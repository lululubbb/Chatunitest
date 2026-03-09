package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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

    private Appendable appendable;
    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        appendable = mock(Appendable.class);
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrintReturnsCSVPrinter() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);

        // Use reflection to get the private 'out' field in CSVPrinter
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Appendable printerOut = (Appendable) outField.get(printer);
        assertSame(appendable, printerOut);

        // Use reflection to get the CSVFormat from the private field in CSVPrinter
        Field formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat printerFormat = (CSVFormat) formatField.get(printer);
        assertEquals(csvFormat, printerFormat);
    }

    @Test
    @Timeout(8000)
    void testPrintThrowsIOException() throws IOException {
        Appendable throwingAppendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("test exception");
            }
            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException("test exception");
            }
            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException("test exception");
            }
        };
        CSVFormat format = CSVFormat.DEFAULT;
        assertThrows(IOException.class, () -> format.print(throwingAppendable));
    }
}