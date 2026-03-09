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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

public class CSVFormat_19_6Test {

    @Test
    @Timeout(8000)
    public void testGetQuoteCharacter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('"', quoteChar.charValue());
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteCharacter_NullQuote() throws Exception {
        // Use reflection to get the MYSQL constant since it uses withQuote(null)
        CSVFormat format = CSVFormat.class.getField("MYSQL").get(null) instanceof CSVFormat
                ? (CSVFormat) CSVFormat.class.getField("MYSQL").get(null)
                : null;
        assertNotNull(format);
        Character quoteChar = format.getQuoteCharacter();
        assertNull(quoteChar);
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteCharacter_CustomQuote() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('#');
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('#', quoteChar.charValue());
    }

    @Test
    @Timeout(8000)
    public void testGetQuoteCharacter_WithQuoteCharacterObject() {
        CSVFormat format = CSVFormat.DEFAULT.withQuote(Character.valueOf('\''));
        Character quoteChar = format.getQuoteCharacter();
        assertNotNull(quoteChar);
        assertEquals('\'', quoteChar.charValue());
    }
}