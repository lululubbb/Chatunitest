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
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

public class CSVFormat_35_5Test {

    @Test
    @Timeout(8000)
    public void testPrintAndEscape() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        StringWriter out = new StringWriter();
        String value = "abc,def";

        // When
        try {
            csvFormat.getClass().getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class).invoke(csvFormat, value, 0, value.length(), out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Then
        assertEquals("abc,def", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscapeWithEscapeChar() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        StringWriter out = new StringWriter();
        String value = "a\"bc,def";

        // When
        try {
            csvFormat.getClass().getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class).invoke(csvFormat, value, 0, value.length(), out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Then
        assertEquals("a\"\"bc,def", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscapeWithDelimiterChar() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        StringWriter out = new StringWriter();
        String value = "a,bc,def";

        // When
        try {
            csvFormat.getClass().getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class).invoke(csvFormat, value, 0, value.length(), out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Then
        assertEquals("a,bc,def", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscapeWithLineBreakChar() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        StringWriter out = new StringWriter();
        String value = "abc\rdef";

        // When
        try {
            csvFormat.getClass().getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class).invoke(csvFormat, value, 0, value.length(), out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Then
        assertEquals("abcndef", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscapeWithEscapeAndLineBreakChar() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        StringWriter out = new StringWriter();
        String value = "a\"bc\rdef";

        // When
        try {
            csvFormat.getClass().getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class).invoke(csvFormat, value, 0, value.length(), out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Then
        assertEquals("a\"\"bcndef", out.toString());
    }

    @Test
    @Timeout(8000)
    public void testPrintAndEscapeWithEscapeAndDelimiterChar() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        StringWriter out = new StringWriter();
        String value = "a\"bc,def";

        // When
        try {
            csvFormat.getClass().getDeclaredMethod("printAndEscape", CharSequence.class, int.class, int.class, Appendable.class).invoke(csvFormat, value, 0, value.length(), out);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Then
        assertEquals("a\"\"bc,def", out.toString());
    }
}