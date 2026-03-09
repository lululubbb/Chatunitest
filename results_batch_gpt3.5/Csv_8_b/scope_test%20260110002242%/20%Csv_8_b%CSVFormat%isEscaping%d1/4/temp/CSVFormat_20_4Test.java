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

public class CSVFormat_20_4Test {

    @Test
    @Timeout(8000)
    public void testIsEscaping_whenEscapeIsNull() throws Exception {
        // Using DEFAULT which has escape == null
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.isEscaping());
    }

    @Test
    @Timeout(8000)
    public void testIsEscaping_whenEscapeIsNotNull() throws Exception {
        // Create a CSVFormat with escape character set using withEscape(char)
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        assertTrue(format.isEscaping());
    }

    @Test
    @Timeout(8000)
    public void testIsEscaping_whenEscapeIsCharacterObjectNotPrimitive() throws Exception {
        // Create CSVFormat with escape set to Character object using reflection to call private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat format = constructor.newInstance(
                CSVFormat.DEFAULT.getDelimiter(),
                CSVFormat.DEFAULT.getQuoteChar(),
                CSVFormat.DEFAULT.getQuotePolicy(),
                CSVFormat.DEFAULT.getCommentStart(),
                (Character) Character.valueOf('!'), // escape as Character object
                CSVFormat.DEFAULT.getIgnoreSurroundingSpaces(),
                CSVFormat.DEFAULT.getIgnoreEmptyLines(),
                CSVFormat.DEFAULT.getRecordSeparator(),
                CSVFormat.DEFAULT.getNullString(),
                CSVFormat.DEFAULT.getHeader(),
                CSVFormat.DEFAULT.getSkipHeaderRecord()
        );

        assertTrue(format.isEscaping());
    }
}