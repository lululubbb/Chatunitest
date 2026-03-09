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

class CSVFormat_15_5Test {

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_whenQuoteCharacterIsSet() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('\'');
        assertEquals(Character.valueOf('\''), format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_whenQuoteCharacterIsNull() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote((Character) null);
        assertNull(format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteCharacter_defaultFormat() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals(Character.valueOf('\"'), format.getQuoteCharacter());
    }
}