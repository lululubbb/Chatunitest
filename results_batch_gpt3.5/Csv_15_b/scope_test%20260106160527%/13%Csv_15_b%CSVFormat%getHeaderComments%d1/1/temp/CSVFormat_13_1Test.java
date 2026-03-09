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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_13_1Test {

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsWhenNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertNull(format.getHeaderComments());
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsClone() throws Exception {
        // Use reflection to create CSVFormat instance with headerComments set
        CSVFormat format = createCSVFormatWithHeaderComments(new String[]{"comment1", "comment2"});
        String[] comments = format.getHeaderComments();

        assertNotNull(comments);
        assertArrayEquals(new String[]{"comment1", "comment2"}, comments);

        // Ensure returned array is a clone, not the same reference
        String[] comments2 = format.getHeaderComments();
        assertNotSame(comments, comments2);
    }

    private CSVFormat createCSVFormatWithHeaderComments(String[] headerComments) throws Exception {
        Class<?> clazz = CSVFormat.class;
        Constructor<?> ctor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class,
                boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class,
                boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        ctor.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;
        QuoteMode quoteMode = defaultFormat.getQuoteMode();

        // headerComments parameter is Object[] in constructor, so cast to Object[]
        Object[] headerCommentsObj = headerComments;

        return (CSVFormat) ctor.newInstance(
                defaultFormat.getDelimiter(),
                defaultFormat.getQuoteCharacter(),
                quoteMode,
                defaultFormat.getCommentMarker(),
                defaultFormat.getEscapeCharacter(),
                defaultFormat.getIgnoreSurroundingSpaces(),
                defaultFormat.getIgnoreEmptyLines(),
                defaultFormat.getRecordSeparator(),
                defaultFormat.getNullString(),
                headerCommentsObj,
                defaultFormat.getHeader(),
                defaultFormat.getSkipHeaderRecord(),
                defaultFormat.getAllowMissingColumnNames(),
                defaultFormat.getIgnoreHeaderCase(),
                defaultFormat.getTrim(),
                defaultFormat.getTrailingDelimiter(),
                defaultFormat.getAutoFlush()
        );
    }
}