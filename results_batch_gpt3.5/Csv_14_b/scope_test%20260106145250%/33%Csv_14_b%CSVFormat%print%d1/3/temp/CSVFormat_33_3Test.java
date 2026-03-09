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
    void testPrint_nullValue_nullString_null() throws IOException {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null).withTrim(false);
        format.print(null, out, true);
        assertEquals("", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringSet() throws IOException {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL").withTrim(false);
        format.print(null, out, true);
        assertEquals("NULL", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_valueCharSequence_noTrim() throws IOException {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withTrim(false);
        format.print("abc", out, false);
        assertEquals("abc", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_valueCharSequence_withTrim() throws IOException {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true);
        format.print(" abc ", out, false);
        assertEquals("abc", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_valueObject_noTrim() throws IOException {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withTrim(false);
        Object val = new Object() {
            @Override
            public String toString() {
                return "val";
            }
        };
        format.print(val, out, false);
        assertEquals("val", out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_valueObject_withTrim() throws IOException {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true);
        Object val = new Object() {
            @Override
            public String toString() {
                return " val ";
            }
        };
        format.print(val, out, false);
        assertEquals("val", out.toString());
    }
}