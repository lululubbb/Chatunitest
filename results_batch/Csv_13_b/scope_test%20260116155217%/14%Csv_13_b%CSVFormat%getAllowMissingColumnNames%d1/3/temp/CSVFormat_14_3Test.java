package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
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

public class CSVFormat_14_3Test {

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_Default() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Using reflection to set allowMissingColumnNames to false to match default
        CSVFormat modifiedFormat = setAllowMissingColumnNames(format, false);
        assertFalse(modifiedFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        // Using reflection to verify the field is true
        assertTrue(getAllowMissingColumnNamesField(format));
        assertTrue(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        // Using reflection to verify the field is false
        assertFalse(getAllowMissingColumnNamesField(format));
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_ExcelConstant() throws Exception {
        CSVFormat format = CSVFormat.EXCEL;
        // Using reflection to verify the field is true
        assertTrue(getAllowMissingColumnNamesField(format));
        assertTrue(format.getAllowMissingColumnNames());
    }

    private CSVFormat setAllowMissingColumnNames(CSVFormat format, boolean value) throws Exception {
        Field delimiterF = CSVFormat.class.getDeclaredField("delimiter");
        Field quoteCharacterF = CSVFormat.class.getDeclaredField("quoteCharacter");
        Field quoteModeF = CSVFormat.class.getDeclaredField("quoteMode");
        Field commentMarkerF = CSVFormat.class.getDeclaredField("commentMarker");
        Field escapeCharacterF = CSVFormat.class.getDeclaredField("escapeCharacter");
        Field ignoreSurroundingSpacesF = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        Field ignoreEmptyLinesF = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        Field recordSeparatorF = CSVFormat.class.getDeclaredField("recordSeparator");
        Field nullStringF = CSVFormat.class.getDeclaredField("nullString");
        Field headerF = CSVFormat.class.getDeclaredField("header");
        Field headerCommentsF = CSVFormat.class.getDeclaredField("headerComments");
        Field skipHeaderRecordF = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        Field ignoreHeaderCaseF = CSVFormat.class.getDeclaredField("ignoreHeaderCase");

        delimiterF.setAccessible(true);
        quoteCharacterF.setAccessible(true);
        quoteModeF.setAccessible(true);
        commentMarkerF.setAccessible(true);
        escapeCharacterF.setAccessible(true);
        ignoreSurroundingSpacesF.setAccessible(true);
        ignoreEmptyLinesF.setAccessible(true);
        recordSeparatorF.setAccessible(true);
        nullStringF.setAccessible(true);
        headerF.setAccessible(true);
        headerCommentsF.setAccessible(true);
        skipHeaderRecordF.setAccessible(true);
        ignoreHeaderCaseF.setAccessible(true);

        char delimiter = delimiterF.getChar(format);
        Character quoteCharacter = (Character) quoteCharacterF.get(format);
        QuoteMode quoteMode = (QuoteMode) quoteModeF.get(format);
        Character commentMarker = (Character) commentMarkerF.get(format);
        Character escapeCharacter = (Character) escapeCharacterF.get(format);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesF.getBoolean(format);
        boolean ignoreEmptyLines = ignoreEmptyLinesF.getBoolean(format);
        String recordSeparator = (String) recordSeparatorF.get(format);
        String nullString = (String) nullStringF.get(format);
        String[] header = (String[]) headerF.get(format);
        String[] headerComments = (String[]) headerCommentsF.get(format);
        boolean skipHeaderRecord = skipHeaderRecordF.getBoolean(format);
        boolean ignoreHeaderCase = ignoreHeaderCaseF.getBoolean(format);

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                String[].class, String[].class, boolean.class, boolean.class, boolean.class
        );
        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                header, headerComments, skipHeaderRecord, value, ignoreHeaderCase
        );
    }

    private boolean getAllowMissingColumnNamesField(CSVFormat format) throws Exception {
        Field field = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        field.setAccessible(true);
        return field.getBoolean(format);
    }
}