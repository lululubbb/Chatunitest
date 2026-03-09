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
        csvFormat = CSVFormat.DEFAULT.withTrim(false).withNullString(null).withQuoteMode(QuoteMode.MINIMAL);
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull_quoteModeNotAll() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null).withQuoteMode(QuoteMode.MINIMAL).withTrim(false);
        Appendable out = mock(Appendable.class);

        // call print with null value and newRecord false
        format.print(null, out, false);

        // verify that appendable append called at least once
        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringSet_quoteModeAll() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL").withQuoteMode(QuoteMode.ALL).withTrim(false);
        Appendable out = mock(Appendable.class);

        format.print(null, out, true);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsCharSequence_trimTrue() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true).withNullString(null).withQuoteMode(QuoteMode.MINIMAL);
        Appendable out = mock(Appendable.class);

        CharSequence value = "  abc  ";
        format.print(value, out, false);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_valueIsNotCharSequence_trimFalse() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(false).withNullString(null).withQuoteMode(QuoteMode.MINIMAL);
        Appendable out = mock(Appendable.class);

        Integer value = 123;
        format.print(value, out, true);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_invokesPrivatePrintMethod() throws Exception {
        Appendable out = mock(Appendable.class);
        String testValue = "test";

        // Use reflection to invoke private print method with CharSequence param
        Method privatePrint = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        privatePrint.setAccessible(true);

        // Call public print to set up charSequence param and call private print
        csvFormat.print(testValue, out, true);

        // Also directly invoke private print to cover it explicitly
        privatePrint.invoke(csvFormat, testValue, testValue, 0, testValue.length(), out, false);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrint_trimMethodViaReflection() throws Exception {
        CharSequence input = "  trim me  ";

        Method trimMethod = CSVFormat.class.getDeclaredMethod("trim", CharSequence.class);
        trimMethod.setAccessible(true);

        CharSequence trimmed = (CharSequence) trimMethod.invoke(csvFormat, input);
        assertEquals("trim me", trimmed.toString());
    }

}