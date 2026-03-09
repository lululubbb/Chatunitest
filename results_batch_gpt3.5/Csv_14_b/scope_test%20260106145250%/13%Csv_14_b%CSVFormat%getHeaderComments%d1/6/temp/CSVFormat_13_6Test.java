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
import java.io.IOException;
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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_13_6Test {

    private CSVFormat createCSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
                                      Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
                                      boolean ignoreEmptyLines, String recordSeparator, String nullString,
                                      Object[] headerComments, String[] header, boolean skipHeaderRecord,
                                      boolean allowMissingColumnNames, boolean ignoreHeaderCase, boolean trim,
                                      boolean trailingDelimiter) {
        try {
            Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                    char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                    boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                    boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
            ctor.setAccessible(true);
            return ctor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                    ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                    headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                    ignoreHeaderCase, trim, trailingDelimiter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void testGetHeaderComments_Null() {
        CSVFormat format = createCSVFormat(',', '"', null, null, null,
                false, true, "\r\n", null, null, null,
                false, false, false, false, false);
        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testGetHeaderComments_CloneIndependence() {
        Object[] comments = new Object[] { "comment1", "comment2" };
        CSVFormat format = createCSVFormat(',', '"', null, null, null,
                false, true, "\r\n", null, comments, null,
                false, false, false, false, false);
        String[] returned = format.getHeaderComments();
        assertNotNull(returned);
        assertArrayEquals(new String[] { "comment1", "comment2" }, returned);
        // Modify returned array and check original is not affected
        returned[0] = "modified";
        String[] returned2 = format.getHeaderComments();
        assertEquals("comment1", returned2[0]);
    }
}