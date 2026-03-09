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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

class CSVFormatHashCodeTest {

    @Test
    @Timeout(8000)
    void testHashCode_defaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_allFieldsDifferent() throws Exception {
        Method newFormatMethod = CSVFormat.class.getDeclaredMethod("newFormat", char.class);
        newFormatMethod.setAccessible(true);
        CSVFormat base = (CSVFormat) newFormatMethod.invoke(null, ';');

        CSVFormat format = base
                .withQuote('?')
                .withQuoteMode(QuoteMode.ALL)
                .withCommentMarker('#')
                .withEscape('\\')
                .withNullString("NULL")
                .withIgnoreSurroundingSpaces(true)
                .withIgnoreHeaderCase(true)
                .withIgnoreEmptyLines(true)
                .withSkipHeaderRecord(true)
                .withRecordSeparator("\n")
                .withHeader("a", "b", "c");

        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_nullFields() throws Exception {
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        ctor.setAccessible(true);
        CSVFormat format = ctor.newInstance(
                ',',
                null,
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                null,
                null,
                false,
                false,
                false);

        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    private int computeExpectedHashCode(CSVFormat format) {
        final int prime = 31;
        int result = 1;

        result = prime * result + format.getDelimiter();
        result = prime * result + ((format.getQuoteMode() == null) ? 0 : format.getQuoteMode().hashCode());
        result = prime * result + ((format.getQuoteCharacter() == null) ? 0 : format.getQuoteCharacter().hashCode());
        result = prime * result + ((format.getCommentMarker() == null) ? 0 : format.getCommentMarker().hashCode());
        result = prime * result + ((format.getEscapeCharacter() == null) ? 0 : format.getEscapeCharacter().hashCode());
        result = prime * result + ((format.getNullString() == null) ? 0 : format.getNullString().hashCode());
        result = prime * result + (format.getIgnoreSurroundingSpaces() ? 1231 : 1237);
        result = prime * result + (format.getIgnoreHeaderCase() ? 1231 : 1237);
        result = prime * result + (format.getIgnoreEmptyLines() ? 1231 : 1237);
        result = prime * result + (format.getSkipHeaderRecord() ? 1231 : 1237);
        result = prime * result + ((format.getRecordSeparator() == null) ? 0 : format.getRecordSeparator().hashCode());
        result = prime * result + Arrays.hashCode(format.getHeader());
        return result;
    }
}