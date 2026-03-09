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
import java.io.IOException;
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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class CSVFormat_23_2Test {

    @Test
    @Timeout(8000)
    void testGetTrimDefaultFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // DEFAULT has trim = false
        assertFalse(format.getTrim());
    }

    @Test
    @Timeout(8000)
    void testGetTrimTrueViaWithTrim() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(true);
        assertTrue(format.getTrim());
    }

    @Test
    @Timeout(8000)
    void testGetTrimFalseViaWithTrim() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withTrim(false);
        assertFalse(format.getTrim());
    }

    @Test
    @Timeout(8000)
    void testGetTrimPrivateFieldManipulation() throws Exception {
        // Use reflection to create a new instance with trim = true
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        ctor.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        CSVFormat format = ctor.newInstance(
                defaultFormat.getDelimiter(),
                defaultFormat.getQuoteCharacter(),
                defaultFormat.getQuoteMode(),
                defaultFormat.getCommentMarker(),
                defaultFormat.getEscapeCharacter(),
                defaultFormat.getIgnoreSurroundingSpaces(),
                defaultFormat.getIgnoreEmptyLines(),
                defaultFormat.getRecordSeparator(),
                defaultFormat.getNullString(),
                null,
                defaultFormat.getHeader(),
                defaultFormat.getSkipHeaderRecord(),
                defaultFormat.getAllowMissingColumnNames(),
                defaultFormat.getIgnoreHeaderCase(),
                true,  // trim = true
                defaultFormat.getTrailingDelimiter(),
                defaultFormat.getAutoFlush()
        );

        assertTrue(format.getTrim());

        // Now set trim = false via reflection on this new instance
        Field trimField = CSVFormat.class.getDeclaredField("trim");
        trimField.setAccessible(true);
        trimField.setBoolean(format, false);

        assertFalse(format.getTrim());
    }
}