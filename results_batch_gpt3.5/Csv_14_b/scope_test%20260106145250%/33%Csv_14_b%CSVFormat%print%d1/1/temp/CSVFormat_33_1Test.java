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

import java.io.IOException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT.withTrim(false);
    }

    @Test
    @Timeout(8000)
    void testPrint_withNullValue_andNullStringNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null).withTrim(false);
        Appendable out = new StringBuilder();
        format.print(null, out, true);
        // Using reflection to verify private print call
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        CharSequence nullString = format.getNullString();
        if (nullString == null) {
            nullString = "";
        }
        // invoke private print with nullString or empty string
        printMethod.invoke(format, null, nullString, 0, nullString.length(), out, true);
        // We just verify no exceptions thrown and output is empty string
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_withNullValue_andNullStringNonNull() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL").withTrim(false);
        Appendable out = new StringBuilder();
        format.print(null, out, false);
        // Since nullString is "NULL", the output should be "NULL"
        assertEquals("NULL", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_withCharSequenceValue_noTrim() throws IOException {
        Appendable out = new StringBuilder();
        String value = "testValue";
        csvFormat.print(value, out, true);
        assertEquals("testValue", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_withCharSequenceValue_withTrim() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true);
        Appendable out = new StringBuilder();
        String value = "  trimmed  ";
        format.print(value, out, false);
        assertEquals("trimmed", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_withNonCharSequenceObject() throws IOException {
        Appendable out = new StringBuilder();
        Object value = 12345;
        csvFormat.print(value, out, true);
        assertEquals("12345", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_invokesPrivatePrintMethod() throws Exception {
        Appendable out = new StringBuilder();
        Object value = "abc";
        CSVFormat format = CSVFormat.DEFAULT.withTrim(false);

        Method privatePrint = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class,
                int.class, Appendable.class, boolean.class);
        privatePrint.setAccessible(true);

        CharSequence charSequence = (CharSequence) value;
        privatePrint.invoke(format, value, charSequence, 0, charSequence.length(), out, true);
        assertEquals("abc", out.toString());
    }
}