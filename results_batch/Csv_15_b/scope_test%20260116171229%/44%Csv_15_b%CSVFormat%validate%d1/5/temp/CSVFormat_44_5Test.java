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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormatValidateTest {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a default CSVFormat instance using reflection to access the private constructor
        // constructor signature:
        // CSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode, Character commentStart,
        //           Character escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
        //           String recordSeparator, String nullString, Object[] headerComments,
        //           String[] header, boolean skipHeaderRecord, boolean allowMissingColumnNames,
        //           boolean ignoreHeaderCase, boolean trim, boolean trailingDelimiter, boolean autoFlush)
        var constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        csvFormat = constructor.newInstance(
                ',', // delimiter
                '"', // quoteChar
                QuoteMode.MINIMAL, // quoteMode
                null, // commentStart
                null, // escape
                false, // ignoreSurroundingSpaces
                false, // ignoreEmptyLines
                "\r\n", // recordSeparator
                null, // nullString
                null, // headerComments
                null, // header
                false, // skipHeaderRecord
                false, // allowMissingColumnNames
                false, // ignoreHeaderCase
                false, // trim
                false, // trailingDelimiter
                false // autoFlush
        );
    }

    private void invokeValidate(CSVFormat format) throws Exception {
        Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
        validateMethod.setAccessible(true);
        validateMethod.invoke(format);
    }

    private CSVFormat createFormatWithFields(Character quoteChar, Character escapeCharacter, Character commentMarker,
            char delimiter, QuoteMode quoteMode, String[] header, boolean allowMissingColumnNames) throws Exception {
        var constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(
                delimiter,
                quoteChar,
                quoteMode,
                commentMarker,
                escapeCharacter,
                false,
                false,
                "\r\n",
                null,
                null,
                header,
                false,
                allowMissingColumnNames,
                false,
                false,
                false,
                false
        );
    }

    @Test
    @Timeout(8000)
    public void testValidate_NoException_Default() throws Exception {
        // Default valid CSVFormat should not throw
        assertDoesNotThrow(() -> invokeValidate(csvFormat));
    }

    @Test
    @Timeout(8000)
    public void testValidate_DelimiterIsLineBreak_Throws() throws Exception {
        // delimiter is line break \n
        CSVFormat format = createFormatWithFields('"', null, null, '\n', QuoteMode.MINIMAL, null, false);
        Exception ex = assertThrows(InvocationTargetException.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assert (cause instanceof IllegalArgumentException);
        assert (cause.getMessage().contains("The delimiter cannot be a line break"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_DelimiterEqualsQuoteChar_Throws() throws Exception {
        // delimiter == quoteChar
        CSVFormat format = createFormatWithFields(',', ',', null, ',', QuoteMode.MINIMAL, null, false);
        Exception ex = assertThrows(InvocationTargetException.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assert (cause instanceof IllegalArgumentException);
        assert (cause.getMessage().contains("quoteChar character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_DelimiterEqualsEscapeChar_Throws() throws Exception {
        CSVFormat format = createFormatWithFields('"', ',', null, ',', QuoteMode.MINIMAL, null, false);
        Exception ex = assertThrows(InvocationTargetException.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assert (cause instanceof IllegalArgumentException);
        assert (cause.getMessage().contains("escape character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_DelimiterEqualsCommentMarker_Throws() throws Exception {
        CSVFormat format = createFormatWithFields('"', null, ',', ',', QuoteMode.MINIMAL, null, false);
        Exception ex = assertThrows(InvocationTargetException.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assert (cause instanceof IllegalArgumentException);
        assert (cause.getMessage().contains("comment start character and the delimiter cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_QuoteCharEqualsCommentMarker_Throws() throws Exception {
        CSVFormat format = createFormatWithFields(',', null, ',', ';', QuoteMode.MINIMAL, null, false);
        // Set quoteChar == commentMarker
        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharField.setAccessible(true);
        quoteCharField.set(format, ',');
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        commentMarkerField.set(format, ',');
        Exception ex = assertThrows(InvocationTargetException.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assert (cause instanceof IllegalArgumentException);
        assert (cause.getMessage().contains("comment start character and the quoteChar cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_EscapeCharEqualsCommentMarker_Throws() throws Exception {
        CSVFormat format = createFormatWithFields(',', '\\', '\\', ';', QuoteMode.MINIMAL, null, false);
        // escapeCharacter == commentMarker == '\\'
        Exception ex = assertThrows(InvocationTargetException.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assert (cause instanceof IllegalArgumentException);
        assert (cause.getMessage().contains("comment start and the escape character cannot be the same"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_NoEscapeCharAndQuoteModeNone_Throws() throws Exception {
        CSVFormat format = createFormatWithFields(',', null, null, ';', QuoteMode.NONE, null, false);
        Exception ex = assertThrows(InvocationTargetException.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assert (cause instanceof IllegalArgumentException);
        assert (cause.getMessage().contains("No quotes mode set but no escape character is set"));
    }

    @Test
    @Timeout(8000)
    public void testValidate_HeaderWithDuplicates_Throws() throws Exception {
        String[] headerWithDuplicates = new String[] { "a", "b", "a" };
        CSVFormat format = createFormatWithFields(',', null, null, ';', QuoteMode.MINIMAL, headerWithDuplicates, false);
        Exception ex = assertThrows(InvocationTargetException.class, () -> invokeValidate(format));
        Throwable cause = ex.getCause();
        assert (cause instanceof IllegalArgumentException);
        assert (cause.getMessage().contains("The header contains a duplicate entry"));
        assert (cause.getMessage().contains("a"));
        assert (cause.getMessage().contains(Arrays.toString(headerWithDuplicates)));
    }

    @Test
    @Timeout(8000)
    public void testValidate_HeaderNoDuplicates_NoException() throws Exception {
        String[] header = new String[] { "a", "b", "c" };
        CSVFormat format = createFormatWithFields(',', null, null, ';', QuoteMode.MINIMAL, header, false);
        assertDoesNotThrow(() -> invokeValidate(format));
    }

    @Test
    @Timeout(8000)
    public void testValidate_AllowMissingColumnNamesTrue_NoException() throws Exception {
        // Even with header duplicates, allowMissingColumnNames true disables exception for duplicates?
        // According to code, duplicates always throw regardless of allowMissingColumnNames, so test no duplicates and allowMissingColumnNames true
        String[] header = new String[] { "a", "b", "c" };
        CSVFormat format = createFormatWithFields(',', null, null, ';', QuoteMode.MINIMAL, header, true);
        assertDoesNotThrow(() -> invokeValidate(format));
    }
}