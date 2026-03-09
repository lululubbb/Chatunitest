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

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull_quoteModeNotAll() throws Exception {
        CSVFormat format = csvFormat.withNullString(null).withQuoteMode(QuoteMode.MINIMAL);
        Appendable out = mock(Appendable.class);

        format.print(null, out, true);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNotNull_quoteModeAll() throws Exception {
        CSVFormat format = csvFormat.withNullString("NULL").withQuoteMode(QuoteMode.ALL);
        Appendable out = mock(Appendable.class);

        format.print(null, out, false);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsCharSequence_trimTrue() throws Exception {
        CSVFormat format = csvFormat.withTrim(true);
        Appendable out = mock(Appendable.class);
        CharSequence value = "  test  ";

        format.print(value, out, false);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsNotCharSequence_trimFalse() throws Exception {
        CSVFormat format = csvFormat.withTrim(false);
        Appendable out = mock(Appendable.class);
        Object value = 12345;

        format.print(value, out, true);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsNotCharSequence_trimTrue() throws Exception {
        CSVFormat format = csvFormat.withTrim(true);
        Appendable out = mock(Appendable.class);
        Object value = 12345;

        format.print(value, out, true);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_privatePrintInvocation() throws Exception {
        Appendable out = mock(Appendable.class);
        String testValue = "value";

        // Use reflection to invoke private print(Object, CharSequence, int, int, Appendable, boolean)
        Method privatePrint = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        privatePrint.setAccessible(true);

        // The 3rd parameter is the start index (0), 4th is length (testValue.length())
        privatePrint.invoke(csvFormat, testValue, testValue, 0, testValue.length(), out, true);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

}