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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_13_4Test {

    @Test
    @Timeout(8000)
    void testGetNullString_whenNullStringIsNull() throws Exception {
        CSVFormat format = createCSVFormatWithNullString(null);
        assertNull(format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_whenNullStringIsSet() throws Exception {
        String nullStr = "NULL";
        CSVFormat format = createCSVFormatWithNullString(nullStr);
        assertEquals(nullStr, format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_withEmptyString() throws Exception {
        String nullStr = "";
        CSVFormat format = createCSVFormatWithNullString(nullStr);
        assertEquals("", format.getNullString());
    }

    private CSVFormat createCSVFormatWithNullString(String nullString) throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;
        Class<?> quotePolicyClass = Class.forName("org.apache.commons.csv.CSVFormat$Quote");
        java.lang.reflect.Constructor<CSVFormat> ctor = clazz.getDeclaredConstructor(
                char.class, Character.class, quotePolicyClass, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        ctor.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        // Pass null explicitly for header (String[]) when default header is null
        String[] header = defaultFormat.getHeader();

        return ctor.newInstance(
                defaultFormat.getDelimiter(),
                defaultFormat.getQuoteChar(),
                defaultFormat.getQuotePolicy(),
                defaultFormat.getCommentStart(),
                defaultFormat.getEscape(),
                defaultFormat.getIgnoreSurroundingSpaces(),
                defaultFormat.getIgnoreEmptyLines(),
                defaultFormat.getRecordSeparator(),
                nullString,
                header,
                defaultFormat.getSkipHeaderRecord()
        );
    }
}