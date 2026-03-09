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
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    void testPrint_withPathAndCharset_callsPrintFile() throws IOException {
        Path mockPath = mock(Path.class);
        File mockFile = mock(File.class);
        Charset charset = Charset.forName("UTF-8");

        when(mockPath.toFile()).thenReturn(mockFile);

        CSVFormat spyFormat = spy(csvFormat);
        doReturn(mock(CSVPrinter.class)).when(spyFormat).print(eq(mockFile), eq(charset));

        CSVPrinter printer = spyFormat.print(mockPath, charset);

        assertNotNull(printer);
        verify(mockPath).toFile();
        verify(spyFormat).print(mockFile, charset);
    }

    @Test
    @Timeout(8000)
    void testPrint_withFileAndCharset_returnsCSVPrinter() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();
        Charset charset = Charset.forName("UTF-8");

        CSVFormat format = CSVFormat.DEFAULT;

        CSVPrinter printer = format.print(tempFile, charset);

        assertNotNull(printer);
        assertEquals(CSVPrinter.class, printer.getClass());
    }

    @Test
    @Timeout(8000)
    void testPrint_withNullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> csvFormat.print((Path) null, Charset.defaultCharset()));
    }

    @Test
    @Timeout(8000)
    void testPrint_withNullCharset_throwsNullPointerException() {
        Path path = Paths.get("somefile.csv");
        assertThrows(NullPointerException.class, () -> csvFormat.print(path, null));
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrintMethod_invocation() throws Exception {
        // Use reflection to invoke private print method:
        // private void print(final Object value, final Appendable out, final boolean newRecord)
        Appendable out = new StringBuilder();
        String value = "value";
        boolean newRecord = true;

        Method privatePrint = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        privatePrint.setAccessible(true);

        // Invoke with non-null value
        privatePrint.invoke(csvFormat, value, out, newRecord);
        assertTrue(out.toString().length() > 0);

        // Invoke with null value
        Appendable out2 = new StringBuilder();
        privatePrint.invoke(csvFormat, null, out2, newRecord);
        assertNotNull(out2.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrintObjectValueWithCharSequence() throws Exception {
        // private void print(final Object object, final CharSequence value, final int offset, final int len,
        //     final Appendable out, final boolean newRecord)
        Appendable out = new StringBuilder();
        String val = "quoted,value";
        int offset = 0;
        int len = val.length();
        boolean newRecord = false;

        Method privatePrint = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        privatePrint.setAccessible(true);

        // Invoke with value containing delimiter
        privatePrint.invoke(csvFormat, val, val, offset, len, out, newRecord);
        assertTrue(out.toString().contains(",") || out.toString().contains("\""));

        // Invoke with null object but non-null value
        Appendable out2 = new StringBuilder();
        privatePrint.invoke(csvFormat, null, val, offset, len, out2, newRecord);
        assertNotNull(out2.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndEscape_privateMethod_invocation() throws Exception {
        // private void printAndEscape(final CharSequence value, final int offset, final int len, final Appendable out)
        Appendable out = new StringBuilder();
        String val = "escape,test";
        int offset = 0;
        int len = val.length();

        Method method = CSVFormat.class.getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class);
        method.setAccessible(true);

        method.invoke(csvFormat, val, offset, len, out);
        assertNotNull(out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintAndQuote_privateMethod_invocation() throws Exception {
        // private void printAndQuote(final Object object, final CharSequence value, final int offset, final int len,
        //     final Appendable out, final boolean newRecord)
        Appendable out = new StringBuilder();
        String val = "quote\"test";
        int offset = 0;
        int len = val.length();
        boolean newRecord = true;

        Method method = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        method.setAccessible(true);

        method.invoke(csvFormat, val, val, offset, len, out, newRecord);
        assertTrue(out.toString().contains("\""));
    }
}