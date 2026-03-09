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

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;

public class CSVFormat_12_6Test {

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNull() throws Exception {
        CSVFormat csvFormat = createCSVFormatWithHeader(null);
        String[] header = csvFormat.getHeader();
        assertNull(header);
    }

    @Test
    @Timeout(8000)
    void testGetHeaderWhenHeaderIsNotNull() throws Exception {
        String[] originalHeader = new String[]{"col1", "col2", "col3"};
        CSVFormat csvFormat = createCSVFormatWithHeader(originalHeader);

        String[] header = csvFormat.getHeader();

        assertNotNull(header);
        assertArrayEquals(originalHeader, header);

        // Verify defensive copy: modifying returned array does not affect internal header
        header[0] = "changed";
        String[] headerAfterChange = csvFormat.getHeader();
        assertArrayEquals(originalHeader, headerAfterChange);
    }

    private CSVFormat createCSVFormatWithHeader(String[] header) throws Exception {
        if (header == null) {
            return CSVFormat.DEFAULT.withHeader((String[]) null);
        } else {
            Field defaultFormatField = CSVFormat.class.getField("DEFAULT");
            CSVFormat defaultFormat = (CSVFormat) defaultFormatField.get(null);

            Class<?> clazz = CSVFormat.class;
            Constructor<?> ctor = clazz.getDeclaredConstructor(
                    char.class, Character.class, QuoteMode.class,
                    Character.class, Character.class, boolean.class,
                    boolean.class, String.class, String.class,
                    Object[].class, String[].class, boolean.class,
                    boolean.class, boolean.class);
            ctor.setAccessible(true);

            Field delimiterField = clazz.getDeclaredField("delimiter");
            Field quoteCharField = clazz.getDeclaredField("quoteCharacter");
            Field quoteModeField = clazz.getDeclaredField("quoteMode");
            Field commentMarkerField = clazz.getDeclaredField("commentMarker");
            Field escapeCharacterField = clazz.getDeclaredField("escapeCharacter");
            Field ignoreSurroundingSpacesField = clazz.getDeclaredField("ignoreSurroundingSpaces");
            Field allowMissingColumnNamesField = clazz.getDeclaredField("allowMissingColumnNames");
            Field ignoreEmptyLinesField = clazz.getDeclaredField("ignoreEmptyLines");
            Field recordSeparatorField = clazz.getDeclaredField("recordSeparator");
            Field nullStringField = clazz.getDeclaredField("nullString");
            Field headerCommentsField = clazz.getDeclaredField("headerComments");
            Field skipHeaderRecordField = clazz.getDeclaredField("skipHeaderRecord");
            Field ignoreHeaderCaseField = clazz.getDeclaredField("ignoreHeaderCase");

            delimiterField.setAccessible(true);
            quoteCharField.setAccessible(true);
            quoteModeField.setAccessible(true);
            commentMarkerField.setAccessible(true);
            escapeCharacterField.setAccessible(true);
            ignoreSurroundingSpacesField.setAccessible(true);
            allowMissingColumnNamesField.setAccessible(true);
            ignoreEmptyLinesField.setAccessible(true);
            recordSeparatorField.setAccessible(true);
            nullStringField.setAccessible(true);
            headerCommentsField.setAccessible(true);
            skipHeaderRecordField.setAccessible(true);
            ignoreHeaderCaseField.setAccessible(true);

            return (CSVFormat) ctor.newInstance(
                    delimiterField.getChar(defaultFormat),
                    (Character) quoteCharField.get(defaultFormat),
                    (QuoteMode) quoteModeField.get(defaultFormat),
                    (Character) commentMarkerField.get(defaultFormat),
                    (Character) escapeCharacterField.get(defaultFormat),
                    ignoreSurroundingSpacesField.getBoolean(defaultFormat),
                    ignoreEmptyLinesField.getBoolean(defaultFormat),
                    (String) recordSeparatorField.get(defaultFormat),
                    (String) nullStringField.get(defaultFormat),
                    (Object[]) headerCommentsField.get(defaultFormat),
                    header,
                    skipHeaderRecordField.getBoolean(defaultFormat),
                    allowMissingColumnNamesField.getBoolean(defaultFormat),
                    ignoreHeaderCaseField.getBoolean(defaultFormat)
            );
        }
    }
}