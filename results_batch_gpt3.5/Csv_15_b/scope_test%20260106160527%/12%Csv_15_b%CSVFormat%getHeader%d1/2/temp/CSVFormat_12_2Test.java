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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Field;

public class CSVFormat_12_2Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testGetHeader_WhenHeaderIsNull() throws Exception {
        // Create CSVFormat instance with null header using reflection
        CSVFormat format = createCSVFormatWithHeader(null);
        String[] header = format.getHeader();
        assertNull(header, "Header should be null when header field is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeader_WhenHeaderIsEmptyArray() throws Exception {
        String[] headerArray = new String[0];
        CSVFormat format = createCSVFormatWithHeader(headerArray);
        String[] header = format.getHeader();
        assertNotNull(header, "Header should not be null when header field is empty array");
        assertArrayEquals(headerArray, header, "Returned header should match the original header array");
        assertNotSame(headerArray, header, "Returned header should be a clone, not the same instance");
    }

    @Test
    @Timeout(8000)
    public void testGetHeader_WhenHeaderHasValues() throws Exception {
        String[] headerArray = new String[] {"col1", "col2", "col3"};
        CSVFormat format = createCSVFormatWithHeader(headerArray);
        String[] header = format.getHeader();
        assertNotNull(header, "Header should not be null when header field has values");
        assertArrayEquals(headerArray, header, "Returned header should match the original header array");
        assertNotSame(headerArray, header, "Returned header should be a clone, not the same instance");

        // Modify returned array and verify original array is not affected
        header[0] = "modified";
        String[] headerAfterModification = getHeaderField(format);
        assertArrayEquals(headerArray, headerAfterModification, "Original header array should remain unchanged");
    }

    // Helper method to create CSVFormat instance with specified header using reflection
    private CSVFormat createCSVFormatWithHeader(String[] header) throws Exception {
        // Create a new CSVFormat instance with the header set via constructor using reflection
        // Because CSVFormat fields are final, we cannot set them on existing instances safely.
        // Instead, create a new instance via the constructor with the header set.

        // Get the constructor with all parameters
        java.lang.reflect.Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);

        // Prepare parameters matching the DEFAULT instance but with header replaced
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        // Extract fields via reflection from DEFAULT to reuse
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        char delimiter = (char) delimiterField.get(defaultFormat);

        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharField.setAccessible(true);
        Character quoteChar = (Character) quoteCharField.get(defaultFormat);

        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(defaultFormat);

        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        commentMarkerField.setAccessible(true);
        Character commentMarker = (Character) commentMarkerField.get(defaultFormat);

        Field escapeCharField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeCharField.setAccessible(true);
        Character escapeChar = (Character) escapeCharField.get(defaultFormat);

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

        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        headerCommentsField.setAccessible(true);
        Object[] headerComments = (Object[]) headerCommentsField.get(defaultFormat);

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

        return constructor.newInstance(
                delimiter,
                quoteChar,
                quoteMode,
                commentMarker,
                escapeChar,
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

    // Helper method to get header field value via reflection
    private String[] getHeaderField(CSVFormat format) throws Exception {
        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        return (String[]) headerField.get(format);
    }
}