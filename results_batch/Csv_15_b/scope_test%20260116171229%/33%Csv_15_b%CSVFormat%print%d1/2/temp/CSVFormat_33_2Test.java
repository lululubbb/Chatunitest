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
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

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
    void testPrint_FileCharset_success() throws IOException {
        File mockFile = mock(File.class);
        Charset charset = Charset.forName("UTF-8");

        // Since mocking constructors is not possible with Mockito 3 without inline,
        // just call the actual method as integration test.
        CSVPrinter printer = csvFormat.print(mockFile, charset);
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    void testPrint_FileCharset_nullCharset() {
        File file = new File("test.csv");
        assertThrows(NullPointerException.class, () -> csvFormat.print(file, null));
    }

    @Test
    @Timeout(8000)
    void testPrint_FileCharset_invalidFile() {
        File file = new File("/invalid/path/to/file.csv");
        Charset charset = Charset.defaultCharset();
        assertThrows(IOException.class, () -> csvFormat.print(file, charset));
    }

    @Test
    @Timeout(8000)
    void testPrint_PrivatePrintMethod() throws Exception {
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        Appendable mockAppendable = mock(Appendable.class);

        printMethod.invoke(csvFormat, "value", "value", 0, 5, mockAppendable, true);
        verify(mockAppendable, atLeastOnce()).append(any(CharSequence.class));

        printMethod.invoke(csvFormat, "value", "value", 0, 5, mockAppendable, false);
        verify(mockAppendable, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_PrivatePrintAndEscapeMethod() throws Exception {
        Method printAndEscape = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class,
                Appendable.class);
        printAndEscape.setAccessible(true);

        Appendable mockAppendable = mock(Appendable.class);

        printAndEscape.invoke(csvFormat, "escapeValue", 0, 11, mockAppendable);
        verify(mockAppendable, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_PrivatePrintAndQuoteMethod() throws Exception {
        Method printAndQuote = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printAndQuote.setAccessible(true);

        Appendable mockAppendable = mock(Appendable.class);

        printAndQuote.invoke(csvFormat, "object", "quoteValue", 0, 10, mockAppendable, true);
        verify(mockAppendable, atLeastOnce()).append(any(CharSequence.class));

        printAndQuote.invoke(csvFormat, "object", "quoteValue", 0, 10, mockAppendable, false);
        verify(mockAppendable, atLeast(2)).append(any(CharSequence.class));
    }
}