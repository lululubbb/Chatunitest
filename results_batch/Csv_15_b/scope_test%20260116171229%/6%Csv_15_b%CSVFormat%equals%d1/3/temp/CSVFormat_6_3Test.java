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
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import java.lang.reflect.Field;
import java.util.Arrays;

class CSVFormatEqualsTest {

    private CSVFormat createCSVFormat(char delimiter,
                                      QuoteMode quoteMode,
                                      Character quoteCharacter,
                                      Character commentMarker,
                                      Character escapeCharacter,
                                      String nullString,
                                      String[] header,
                                      boolean ignoreSurroundingSpaces,
                                      boolean ignoreEmptyLines,
                                      boolean skipHeaderRecord,
                                      String recordSeparator) throws Exception {
        // Use reflection to invoke private constructor
        var constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        // Pass false for allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter, autoFlush
        return (CSVFormat) constructor.newInstance(
                delimiter,
                quoteCharacter,
                quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                null,
                header,
                skipHeaderRecord,
                false,
                false,
                false,
                false,
                false,
                false
        );
    }

    @Test
    @Timeout(8000)
    void testEquals_sameInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullObject() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentClass() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.equals("not a CSVFormat"));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentDelimiter() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(';', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentQuoteMode() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.MINIMAL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, null, '#', '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_quoteCharacterNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, null, '#', '\\',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentQuoteCharacter() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '\'', '#', '\\',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarkerNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', null, '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_commentMarkerNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', null, '\\',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentCommentMarker() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '!', '\\',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacterNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', null,
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_escapeCharacterNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', null,
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentEscapeCharacter() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '!',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullStringNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                "NULL", null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_nullStringNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                "NULL", null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentNullString() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                "NULL", null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                "null", null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentHeader() throws Exception {
        String[] header1 = new String[] {"a", "b"};
        String[] header2 = new String[] {"a", "c"};
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, header1, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, header2, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_headerNullVsNonNull() throws Exception {
        String[] header = new String[] {"a", "b"};
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, header, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_headerNonNullVsNull() throws Exception {
        String[] header = new String[] {"a", "b"};
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, header, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreSurroundingSpacesDiffers() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, true, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_ignoreEmptyLinesDiffers() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, false, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_skipHeaderRecordDiffers() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparatorNullVsNonNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, null);
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_recordSeparatorNonNullVsNull() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, null);
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_differentRecordSeparator() throws Exception {
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                null, null, false, true, false, "\n");
        assertFalse(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    void testEquals_allFieldsEqual() throws Exception {
        String[] header = new String[] {"a", "b"};
        CSVFormat format1 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                "NULL", header, true, false, true, "\r\n");
        CSVFormat format2 = createCSVFormat(',', QuoteMode.ALL_NON_NULL, '"', '#', '\\',
                "NULL", header.clone(), true, false, true, "\r\n");
        assertTrue(format1.equals(format2));
    }
}