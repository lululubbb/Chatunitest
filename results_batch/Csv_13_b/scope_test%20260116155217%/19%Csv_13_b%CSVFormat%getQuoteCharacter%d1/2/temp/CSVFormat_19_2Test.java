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
import org.junit.jupiter.api.DisplayName;

class CSVFormat_19_2Test {

    @Test
    @Timeout(8000)
    @DisplayName("Test getQuoteCharacter returns correct quote character when set")
    void testGetQuoteCharacterWhenSet() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('\'');
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('\'', quoteChar.charValue());
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test getQuoteCharacter returns null when quote character is set to null")
    void testGetQuoteCharacterWhenNull() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote((Character) null);
        Character quoteChar = format.getQuoteCharacter();
        assertNull(quoteChar);
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test getQuoteCharacter returns default quote character when not changed")
    void testGetQuoteCharacterDefault() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), quoteChar);
    }
}