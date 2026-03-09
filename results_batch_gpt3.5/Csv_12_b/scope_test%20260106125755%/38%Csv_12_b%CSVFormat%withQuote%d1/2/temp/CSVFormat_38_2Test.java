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

public class CSVFormat_38_2Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_char() {
        // Use a normal quote character
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\'';
        CSVFormat updated = original.withQuote(quoteChar);
        assertNotNull(updated);
        assertEquals(Character.valueOf(quoteChar), updated.getQuoteCharacter());

        // Use double quote character (default)
        updated = original.withQuote('"');
        assertNotNull(updated);
        assertEquals(Character.valueOf('"'), updated.getQuoteCharacter());

        // Use a control character as quote character
        updated = original.withQuote('\u0000');
        assertNotNull(updated);
        assertEquals(Character.valueOf('\u0000'), updated.getQuoteCharacter());

        // Use a non-ASCII char
        updated = original.withQuote('ß');
        assertNotNull(updated);
        assertEquals(Character.valueOf('ß'), updated.getQuoteCharacter());

        // Use a quote character that is same as delimiter
        updated = original.withQuote(original.getDelimiter());
        assertNotNull(updated);
        assertEquals(Character.valueOf(original.getDelimiter()), updated.getQuoteCharacter());

        // Use quote character as backslash
        updated = original.withQuote('\\');
        assertNotNull(updated);
        assertEquals(Character.valueOf('\\'), updated.getQuoteCharacter());
    }
}