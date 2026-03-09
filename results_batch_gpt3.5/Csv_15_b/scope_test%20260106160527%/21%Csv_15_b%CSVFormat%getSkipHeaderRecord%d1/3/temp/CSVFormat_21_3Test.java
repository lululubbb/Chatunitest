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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormat_21_3Test {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatSkipTrue;
    private CSVFormat csvFormatSkipFalse;

    @BeforeEach
    void setUp() throws Exception {
        csvFormatDefault = CSVFormat.DEFAULT;
        csvFormatSkipTrue = withSkipHeaderRecord(csvFormatDefault, true);
        csvFormatSkipFalse = withSkipHeaderRecord(csvFormatDefault, false);
    }

    private CSVFormat withSkipHeaderRecord(CSVFormat original, boolean skip) throws Exception {
        try {
            return (CSVFormat) CSVFormat.class.getMethod("withSkipHeaderRecord", boolean.class).invoke(original, skip);
        } catch (NoSuchMethodException e) {
            // Fallback: use reflection to create new instance with modified skipHeaderRecord

            Object[] params = new Object[17];

            Field delimiterF = CSVFormat.class.getDeclaredField("delimiter");
            delimiterF.setAccessible(true);
            params[0] = delimiterF.getChar(original);

            Field quoteCharF = CSVFormat.class.getDeclaredField("quoteCharacter");
            quoteCharF.setAccessible(true);
            params[1] = (Character) quoteCharF.get(original);

            Field quoteModeF = CSVFormat.class.getDeclaredField("quoteMode");
            quoteModeF.setAccessible(true);
            params[2] = quoteModeF.get(original);

            Field commentMarkerF = CSVFormat.class.getDeclaredField("commentMarker");
            commentMarkerF.setAccessible(true);
            params[3] = (Character) commentMarkerF.get(original);

            Field escapeCharacterF = CSVFormat.class.getDeclaredField("escapeCharacter");
            escapeCharacterF.setAccessible(true);
            params[4] = (Character) escapeCharacterF.get(original);

            Field ignoreSurroundingSpacesF = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
            ignoreSurroundingSpacesF.setAccessible(true);
            params[5] = ignoreSurroundingSpacesF.getBoolean(original);

            Field ignoreEmptyLinesF = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
            ignoreEmptyLinesF.setAccessible(true);
            params[6] = ignoreEmptyLinesF.getBoolean(original);

            Field recordSeparatorF = CSVFormat.class.getDeclaredField("recordSeparator");
            recordSeparatorF.setAccessible(true);
            params[7] = (String) recordSeparatorF.get(original);

            Field nullStringF = CSVFormat.class.getDeclaredField("nullString");
            nullStringF.setAccessible(true);
            params[8] = (String) nullStringF.get(original);

            Field headerCommentsF = CSVFormat.class.getDeclaredField("headerComments");
            headerCommentsF.setAccessible(true);
            params[9] = (Object[]) headerCommentsF.get(original);

            Field headerF = CSVFormat.class.getDeclaredField("header");
            headerF.setAccessible(true);
            params[10] = (String[]) headerF.get(original);

            params[11] = skip;

            Field allowMissingColumnNamesF = CSVFormat.class.getDeclaredField("allowMissingColumnNames");
            allowMissingColumnNamesF.setAccessible(true);
            params[12] = allowMissingColumnNamesF.getBoolean(original);

            Field ignoreHeaderCaseF = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
            ignoreHeaderCaseF.setAccessible(true);
            params[13] = ignoreHeaderCaseF.getBoolean(original);

            Field trimF = CSVFormat.class.getDeclaredField("trim");
            trimF.setAccessible(true);
            params[14] = trimF.getBoolean(original);

            Field trailingDelimiterF = CSVFormat.class.getDeclaredField("trailingDelimiter");
            trailingDelimiterF.setAccessible(true);
            params[15] = trailingDelimiterF.getBoolean(original);

            Field autoFlushF = CSVFormat.class.getDeclaredField("autoFlush");
            autoFlushF.setAccessible(true);
            params[16] = autoFlushF.getBoolean(original);

            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                    char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                    boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                    boolean.class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            return constructor.newInstance(params);
        }
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_default() {
        assertFalse(csvFormatDefault.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_withSkipTrue() {
        assertTrue(csvFormatSkipTrue.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_withSkipFalse() {
        assertFalse(csvFormatSkipFalse.getSkipHeaderRecord());
    }
}