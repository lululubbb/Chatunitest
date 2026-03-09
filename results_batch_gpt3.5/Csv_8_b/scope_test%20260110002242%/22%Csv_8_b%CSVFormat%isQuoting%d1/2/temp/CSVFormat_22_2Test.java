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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_22_2Test {

    @Test
    @Timeout(8000)
    void testIsQuoting_withQuoteCharNotNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.isQuoting());
    }

    @Test
    @Timeout(8000)
    void testIsQuoting_withQuoteCharNull() throws Exception {
        // Use reflection to create a CSVFormat instance with quoteChar=null
        java.lang.reflect.Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, CSVFormat.Quote.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                CSVFormat.DEFAULT.getDelimiter(),
                null,
                null,
                CSVFormat.DEFAULT.getCommentStart(),
                CSVFormat.DEFAULT.getEscape(),
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord()
        );
        assertFalse(format.isQuoting());
    }

}