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

class CSVFormat_31_5Test {

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

        // Use reflection to get the private 'out' field from CSVPrinter
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object out = outField.get(printer);
        assertSame(appendable, out);

        // Use reflection to get the private 'format' field from CSVPrinter
        Field formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object format = formatField.get(printer);
        assertEquals(csvFormat, format);
    }

    @Test
    @Timeout(8000)
    void testPrint_WithDifferentCSVFormat() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVFormat format = CSVFormat.EXCEL.withDelimiter(';').withQuote('|');
        Appendable appendable = new StringBuilder();
        CSVPrinter printer = format.print(appendable);
        assertNotNull(printer);

        // Use reflection to get the private 'out' field from CSVPrinter
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object out = outField.get(printer);
        assertSame(appendable, out);

        // Use reflection to get the private 'format' field from CSVPrinter
        Field formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object printerFormat = formatField.get(printer);

        assertEquals(format, printerFormat);
        assertEquals(';', ((CSVFormat) printerFormat).getDelimiter());
        assertEquals(Character.valueOf('|'), ((CSVFormat) printerFormat).getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testPrint_ThrowsIOException() {
        Appendable appendable = mock(Appendable.class);
        try {
            doThrow(new IOException("test exception")).when(appendable).append(any(CharSequence.class));
        } catch (IOException e) {
            fail("Mock setup failure");
        }
        // CSVFormat.print does not invoke append, so IOException won't be thrown here.
        // But CSVPrinter constructor does not throw IOException.
        // So we test that print does not throw IOException even if Appendable is mock throwing IOException on append.
        assertDoesNotThrow(() -> csvFormat.print(appendable));
    }
}