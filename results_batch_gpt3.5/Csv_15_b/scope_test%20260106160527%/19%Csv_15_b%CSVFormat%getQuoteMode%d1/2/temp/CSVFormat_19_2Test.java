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

public class CSVFormat_19_2Test {

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.MINIMAL, quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_CustomQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        QuoteMode quoteMode = format.getQuoteMode();
        assertNotNull(quoteMode);
        assertEquals(QuoteMode.ALL, quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_NullQuoteMode() throws Exception {
        // Using reflection to create a CSVFormat instance with null quoteMode
        // since withQuoteMode(null) may not be allowed or may cause compilation issues.
        CSVFormat defaultFormat = CSVFormat.DEFAULT;
        CSVFormat formatWithNullQuoteMode = createCSVFormatWithNullQuoteMode(defaultFormat);
        QuoteMode quoteMode = formatWithNullQuoteMode.getQuoteMode();
        assertNull(quoteMode);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteMode_OtherPredefinedFormats() {
        assertEquals(QuoteMode.MINIMAL, CSVFormat.EXCEL.getQuoteMode());
        assertEquals(QuoteMode.MINIMAL, CSVFormat.INFORMIX_UNLOAD.getQuoteMode());
        assertEquals(QuoteMode.MINIMAL, CSVFormat.INFORMIX_UNLOAD_CSV.getQuoteMode());
        assertEquals(QuoteMode.ALL_NON_NULL, CSVFormat.MYSQL.getQuoteMode());
        assertEquals(QuoteMode.ALL_NON_NULL, CSVFormat.POSTGRESQL_CSV.getQuoteMode());
        assertEquals(QuoteMode.ALL_NON_NULL, CSVFormat.POSTGRESQL_TEXT.getQuoteMode());
        assertEquals(QuoteMode.MINIMAL, CSVFormat.RFC4180.getQuoteMode());
        assertEquals(QuoteMode.MINIMAL, CSVFormat.TDF.getQuoteMode());
    }

    private CSVFormat createCSVFormatWithNullQuoteMode(CSVFormat baseFormat) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        char delimiter = baseFormat.getDelimiter();
        Character quoteChar = baseFormat.getQuoteCharacter();
        Character commentStart = baseFormat.getCommentMarker();
        Character escape = baseFormat.getEscapeCharacter();
        boolean ignoreSurroundingSpaces = baseFormat.getIgnoreSurroundingSpaces();
        boolean ignoreEmptyLines = baseFormat.getIgnoreEmptyLines();
        String recordSeparator = baseFormat.getRecordSeparator();
        String nullString = baseFormat.getNullString();
        Object[] headerComments = null; // baseFormat.getHeaderComments() not available, so null
        String[] header = baseFormat.getHeader();
        boolean skipHeaderRecord = baseFormat.getSkipHeaderRecord();
        boolean allowMissingColumnNames = baseFormat.getAllowMissingColumnNames();
        boolean ignoreHeaderCase = baseFormat.getIgnoreHeaderCase();
        boolean trim = baseFormat.getTrim();
        boolean trailingDelimiter = baseFormat.getTrailingDelimiter();
        boolean autoFlush = baseFormat.getAutoFlush();

        return constructor.newInstance(
                delimiter,
                quoteChar,
                null, // quoteMode set to null
                commentStart,
                escape,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                headerComments,
                header,
                skipHeaderRecord,
                allowMissingColumnNames,
                ignoreHeaderCase,
                trim,
                trailingDelimiter,
                autoFlush);
    }
}