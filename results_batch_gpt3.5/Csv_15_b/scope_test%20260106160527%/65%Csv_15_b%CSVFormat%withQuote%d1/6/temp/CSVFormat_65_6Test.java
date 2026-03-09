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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CSVFormat_65_6Test {

    @Test
    @Timeout(8000)
    public void testWithQuotePrimitiveChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char quoteChar = '\'';
        CSVFormat updated = original.withQuote(quoteChar);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(Character.valueOf(quoteChar), updated.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteCharacterNull() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withQuote((Character) null);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertNull(updated.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteCharacterSameValue() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character quoteChar = original.getQuoteCharacter();
        if (quoteChar == null) {
            quoteChar = Character.valueOf('"');
        }
        CSVFormat updated = original.withQuote(quoteChar);

        assertSame(original, updated);
    }

    @Test
    @Timeout(8000)
    public void testWithQuoteCharacterDifferentValue() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character newQuoteChar = Character.valueOf('\'');
        if (newQuoteChar.equals(original.getQuoteCharacter())) {
            newQuoteChar = Character.valueOf('\"');
        }
        CSVFormat updated = original.withQuote(newQuoteChar);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(newQuoteChar, updated.getQuoteCharacter());
    }
}