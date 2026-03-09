package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormatWithQuoteTest {

    @Test
    @Timeout(8000)
    void testWithQuote_withChar_createsNewInstanceWithCorrectQuoteCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\'';

        CSVFormat updated = original.withQuote(quoteChar);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(Character.valueOf(quoteChar), updated.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withChar_nullCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\0';

        CSVFormat updated = original.withQuote(quoteChar);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(Character.valueOf(quoteChar), updated.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withChar_sameAsOriginalQuoteCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character originalQuote = original.getQuoteCharacter();
        char quoteChar = originalQuote != null ? originalQuote : '\0';

        CSVFormat updated = original.withQuote(quoteChar);

        assertNotNull(updated);
        // The original withQuote(char) returns a new instance even if the quote char is the same
        assertNotSame(original, updated);
        assertEquals(Character.valueOf(quoteChar), updated.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_withChar_integrationWithWithQuoteCharacterNull() {
        CSVFormat original = CSVFormat.DEFAULT.withQuote((Character) null);
        char quoteChar = '\"';

        CSVFormat updated = original.withQuote(quoteChar);

        assertNotNull(updated);
        assertEquals(Character.valueOf(quoteChar), updated.getQuoteCharacter());
    }
}