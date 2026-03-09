package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
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

import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.TAB;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class CSVFormat_10_2Test {

    @Test
    @Timeout(8000)
    public void testGetDelimiter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals(COMMA, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_Excel() {
        CSVFormat format = CSVFormat.EXCEL;
        assertEquals(COMMA, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_InformixUnload() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD;
        assertEquals(PIPE, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_InformixUnloadCsv() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD_CSV;
        assertEquals(COMMA, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_Mysql() {
        CSVFormat format = CSVFormat.MYSQL;
        assertEquals(TAB, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_PostgresqlCsv() {
        CSVFormat format = CSVFormat.POSTGRESQL_CSV;
        assertEquals(COMMA, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_PostgresqlText() {
        CSVFormat format = CSVFormat.POSTGRESQL_TEXT;
        assertEquals(TAB, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_Rfc4180() throws Exception {
        CSVFormat format = CSVFormat.RFC4180;
        // RFC4180 is DEFAULT.withIgnoreEmptyLines(false), delimiter remains COMMA
        assertEquals(COMMA, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_Tdf() {
        CSVFormat format = CSVFormat.TDF;
        assertEquals(TAB, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_CustomViaReflection() throws Exception {
        // Create new instance with custom delimiter using reflection since CSVFormat is immutable
        CSVFormat format = CSVFormat.DEFAULT;
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);

        // Create a new CSVFormat instance with the custom delimiter by copying all fields from DEFAULT
        // but replacing delimiter with ';'
        // Since CSVFormat is immutable, changing the field on DEFAULT directly is unsafe.
        // Instead, create a new instance via reflection with delimiter ';'.

        // Retrieve all fields needed for constructor
        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteCharacter");
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        Field commentMarkerField = CSVFormat.class.getDeclaredField("commentMarker");
        Field escapeCharacterField = CSVFormat.class.getDeclaredField("escapeCharacter");
        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        Field headerCommentsField = CSVFormat.class.getDeclaredField("headerComments");
        Field headerField = CSVFormat.class.getDeclaredField("header");
        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        Field allowMissingColumnNamesField = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
        Field ignoreHeaderCaseField = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        Field trimField = CSVFormat.class.getDeclaredField("trim");
        Field trailingDelimiterField = CSVFormat.class.getDeclaredField("trailingDelimiter");
        Field autoFlushField = CSVFormat.class.getDeclaredField("autoFlush");

        quoteCharField.setAccessible(true);
        quoteModeField.setAccessible(true);
        commentMarkerField.setAccessible(true);
        escapeCharacterField.setAccessible(true);
        ignoreSurroundingSpacesField.setAccessible(true);
        ignoreEmptyLinesField.setAccessible(true);
        recordSeparatorField.setAccessible(true);
        nullStringField.setAccessible(true);
        headerCommentsField.setAccessible(true);
        headerField.setAccessible(true);
        skipHeaderRecordField.setAccessible(true);
        allowMissingColumnNamesField.setAccessible(true);
        ignoreHeaderCaseField.setAccessible(true);
        trimField.setAccessible(true);
        trailingDelimiterField.setAccessible(true);
        autoFlushField.setAccessible(true);

        char delimiter = ';';
        Character quoteChar = (Character) quoteCharField.get(format);
        QuoteMode quoteMode = (QuoteMode) quoteModeField.get(format);
        Character commentMarker = (Character) commentMarkerField.get(format);
        Character escapeCharacter = (Character) escapeCharacterField.get(format);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(format);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(format);
        String recordSeparator = (String) recordSeparatorField.get(format);
        String nullString = (String) nullStringField.get(format);
        Object[] headerComments = (Object[]) headerCommentsField.get(format);
        String[] header = (String[]) headerField.get(format);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(format);
        boolean allowMissingColumnNames = allowMissingColumnNamesField.getBoolean(format);
        boolean ignoreHeaderCase = ignoreHeaderCaseField.getBoolean(format);
        boolean trim = trimField.getBoolean(format);
        boolean trailingDelimiter = trailingDelimiterField.getBoolean(format);
        boolean autoFlush = autoFlushField.getBoolean(format);

        // Find constructor
        java.lang.reflect.Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat customFormat = constructor.newInstance(
                delimiter, quoteChar, quoteMode, commentMarker, escapeCharacter,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                headerComments, header, skipHeaderRecord, allowMissingColumnNames,
                ignoreHeaderCase, trim, trailingDelimiter, autoFlush);

        assertEquals(';', customFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_NewFormat() {
        CSVFormat format = CSVFormat.newFormat('#');
        assertEquals('#', format.getDelimiter());
    }
}