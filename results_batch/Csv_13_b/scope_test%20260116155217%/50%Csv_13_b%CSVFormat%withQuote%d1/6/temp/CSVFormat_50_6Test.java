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

class CSVFormat_50_6Test {

    @Test
    @Timeout(8000)
    void testWithQuote_char() {
        // Arrange
        CSVFormat format = CSVFormat.DEFAULT;

        // Act
        CSVFormat result = format.withQuote('"');

        // Assert
        assertNotNull(result);
        assertEquals(Character.valueOf('"'), result.getQuoteCharacter());
        // Original format should remain unchanged (immutability)
        assertEquals(Character.valueOf('"'), CSVFormat.DEFAULT.getQuoteCharacter());
        assertEquals(Character.valueOf('"'), format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_char_nullQuote() {
        // Arrange
        CSVFormat format = CSVFormat.DEFAULT;

        // Act
        CSVFormat result = format.withQuote((Character) null);

        // Assert
        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        assertNotSame(format, result);
    }
}