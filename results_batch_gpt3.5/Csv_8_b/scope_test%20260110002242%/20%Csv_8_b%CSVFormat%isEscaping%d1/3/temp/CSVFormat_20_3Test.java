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

import java.lang.reflect.Constructor;

class CSVFormat_20_3Test {

    @Test
    @Timeout(8000)
    void testIsEscaping_whenEscapeIsNull_returnsFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, CSVFormat.Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat formatWithNullEscape = constructor.newInstance(
                format.getDelimiter(),
                format.getQuoteChar(),
                format.getQuotePolicy(),
                format.getCommentStart(),
                null, // escape set to null
                format.getIgnoreSurroundingSpaces(),
                format.getIgnoreEmptyLines(),
                format.getRecordSeparator(),
                format.getNullString(),
                format.getHeader(),
                format.getSkipHeaderRecord()
        );

        assertFalse(formatWithNullEscape.isEscaping());
    }

    @Test
    @Timeout(8000)
    void testIsEscaping_whenEscapeIsNonNull_returnsTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        assertTrue(format.isEscaping());
    }
}