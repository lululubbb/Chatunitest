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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class CSVFormat_18_3Test {

    @Test
    @Timeout(8000)
    public void testHashCode_withDefaultValues() {
        CSVFormat format = CSVFormat.DEFAULT;
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_withCustomValues() throws Exception {
        String[] header = new String[]{"A", "B", "C"};
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                Quote.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                String[].class,
                boolean.class
        );
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                ';',
                Character.valueOf('\"'),
                Quote.ALL,
                Character.valueOf('#'),
                Character.valueOf('\\'),
                true,
                false,
                "\n",
                "NULL",
                header,
                true
        );
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_withNullFields() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                Quote.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                String[].class,
                boolean.class
        );
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                ',',
                null,
                null,
                null,
                null,
                false,
                false,
                null,
                null,
                (String[]) null,
                false
        );
        int expected = computeExpectedHashCode(format);
        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    public void testHashCode_consistency() {
        CSVFormat format = CSVFormat.DEFAULT;
        int hash1 = format.hashCode();
        int hash2 = format.hashCode();
        assertEquals(hash1, hash2);
    }

    private int computeExpectedHashCode(CSVFormat format) {
        final int prime = 31;
        int result = 1;
        result = prime * result + format.getDelimiter();
        result = prime * result + (format.getQuotePolicy() == null ? 0 : format.getQuotePolicy().hashCode());
        result = prime * result + (format.getQuoteChar() == null ? 0 : format.getQuoteChar().hashCode());
        result = prime * result + (format.getCommentStart() == null ? 0 : format.getCommentStart().hashCode());
        result = prime * result + (format.getEscape() == null ? 0 : format.getEscape().hashCode());
        result = prime * result + (format.getNullString() == null ? 0 : format.getNullString().hashCode());
        result = prime * result + (format.getIgnoreSurroundingSpaces() ? 1231 : 1237);
        result = prime * result + (format.getIgnoreEmptyLines() ? 1231 : 1237);
        result = prime * result + (format.getSkipHeaderRecord() ? 1231 : 1237);
        result = prime * result + (format.getRecordSeparator() == null ? 0 : format.getRecordSeparator().hashCode());
        result = prime * result + Arrays.hashCode(format.getHeader());
        return result;
    }
}