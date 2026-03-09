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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_24_1Test {

    @Test
    @Timeout(8000)
    void testGetAutoFlush_DefaultFalse() {
        // Using DEFAULT constant which has autoFlush = false
        CSVFormat format = CSVFormat.DEFAULT;

        assertFalse(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testGetAutoFlush_TrueViaReflection() throws Exception {
        // Create a CSVFormat instance via reflection to set autoFlush = true
        CSVFormat format = createCSVFormatWithAutoFlush(true);

        assertTrue(format.getAutoFlush());
    }

    @Test
    @Timeout(8000)
    void testGetAutoFlush_FalseViaReflection() throws Exception {
        // Create a CSVFormat instance via reflection to set autoFlush = false
        CSVFormat format = createCSVFormatWithAutoFlush(false);

        assertFalse(format.getAutoFlush());
    }

    private CSVFormat createCSVFormatWithAutoFlush(boolean autoFlush) throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;

        Constructor<CSVFormat> ctor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);

        ctor.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        return ctor.newInstance(
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
                defaultFormat.getTrim(),
                defaultFormat.getTrailingDelimiter(),
                autoFlush);
    }
}