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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Constructor;

public class CSVFormat_13_3Test {

    private static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsCloneWhenNotNull() throws Exception {
        // Create a new CSVFormat instance with headerComments set via reflection
        String[] comments = new String[] {"comment1", "comment2"};

        // Create a new CSVFormat instance based on DEFAULT but with headerComments set
        CSVFormat format = createCSVFormatWithHeaderComments(comments);

        String[] result = format.getHeaderComments();

        assertNotNull(result);
        assertArrayEquals(comments, result);
        assertNotSame(comments, result, "Returned array should be a clone, not the same instance");
    }

    @Test
    @Timeout(8000)
    void testGetHeaderCommentsReturnsNullWhenNull() throws Exception {
        // Create a new CSVFormat instance with headerComments null
        CSVFormat format = createCSVFormatWithHeaderComments(null);

        String[] result = format.getHeaderComments();

        assertNull(result);
    }

    private CSVFormat createCSVFormatWithHeaderComments(String[] headerComments) throws Exception {
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = (char) delimiterField.get(defaultFormat);

        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);
        Character quoteCharacter = (Character) quoteCharacterField.get(defaultFormat);

        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(defaultFormat);

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(defaultFormat);

        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharacterField.setAccessible(true);
        Character escapeCharacter = (Character) escapeCharacterField.get(defaultFormat);

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        boolean ignoreSurroundingSpaces = (boolean) ignoreSurroundingSpacesField.get(defaultFormat);

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        boolean ignoreEmptyLines = (boolean) ignoreEmptyLinesField.get(defaultFormat);

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        String recordSeparator = (String) recordSeparatorField.get(defaultFormat);

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String nullString = (String) nullStringField.get(defaultFormat);

        // Use headerComments array directly (not wrapped in Object[])
        Object[] headerCommentsObj = headerComments;

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] header = (String[]) headerField.get(defaultFormat);

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        boolean skipHeaderRecord = (boolean) skipHeaderRecordField.get(defaultFormat);

        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        allowMissingColumnNamesField.setAccessible(true);
        boolean allowMissingColumnNames = (boolean) allowMissingColumnNamesField.get(defaultFormat);

        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        ignoreHeaderCaseField.setAccessible(true);
        boolean ignoreHeaderCase = (boolean) ignoreHeaderCaseField.get(defaultFormat);

        Field trimField = CSVFormat.class.getDeclaredField("trim");
        trimField.setAccessible(true);
        boolean trim = (boolean) trimField.get(defaultFormat);

        Field trailingDelimiterField = CSVFormat.class.getDeclaredField("trailingDelimiter");
        trailingDelimiterField.setAccessible(true);
        boolean trailingDelimiter = (boolean) trailingDelimiterField.get(defaultFormat);

        Field autoFlushField = CSVFormat.class.getDeclaredField("autoFlush");
        autoFlushField.setAccessible(true);
        boolean autoFlush = (boolean) autoFlushField.get(defaultFormat);

        // Constructor parameter types must match exactly, including Object[] for headerComments
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class
        );
        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter,
                quoteCharacter,
                quoteMode,
                commentMarker,
                escapeCharacter,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                headerCommentsObj,
                header,
                skipHeaderRecord,
                allowMissingColumnNames,
                ignoreHeaderCase,
                trim,
                trailingDelimiter,
                autoFlush
        );
    }
}