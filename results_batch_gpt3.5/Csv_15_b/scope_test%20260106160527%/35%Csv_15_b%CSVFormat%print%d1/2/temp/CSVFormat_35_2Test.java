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
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;
    private Appendable appendable;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull_quoteModeNotAll() throws IOException {
        CSVFormat format = csvFormat.withNullString(null).withQuoteMode(QuoteMode.MINIMAL);
        format.print(null, appendable, false);
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringSet_quoteModeAll() throws IOException {
        CSVFormat format = csvFormat.withNullString("NULL").withQuoteMode(QuoteMode.ALL);
        format.print(null, appendable, true);
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsCharSequence_trimTrue() throws IOException {
        CSVFormat format = csvFormat.withTrim(true);
        CharSequence cs = "  test  ";
        format.print(cs, appendable, false);
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsNotCharSequence_trimFalse() throws IOException {
        CSVFormat format = csvFormat.withTrim(false);
        Object value = 12345;
        format.print(value, appendable, true);
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_invokesPrivatePrintMethod() throws Exception {
        CharSequence cs = "value";
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        Appendable appendableSpy = spy(new StringBuilder());
        csvFormat.print("value", appendableSpy, true);

        printMethod.invoke(csvFormat, "value", cs, 0, cs.length(), appendableSpy, true);

        assertTrue(appendableSpy.toString().length() > 0);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringSet_quoteModeNotAll() throws IOException {
        CSVFormat format = csvFormat.withNullString("NULL").withQuoteMode(QuoteMode.MINIMAL);
        format.print(null, appendable, false);
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull_quoteModeAll() throws IOException {
        CSVFormat format = csvFormat.withNullString(null).withQuoteMode(QuoteMode.ALL);
        format.print(null, appendable, false);
        verify(appendable, atLeastOnce()).append(any(CharSequence.class));
    }
}