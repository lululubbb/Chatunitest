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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

class CSVFormat_65_4Test {

    @Test
    @Timeout(8000)
    void testWithQuote_primitiveChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char quoteChar = '\'';
        CSVFormat result = format.withQuote(quoteChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
        // original format remains unchanged (immutability)
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_nullCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withQuote((Character) null);
        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithQuote_sameQuoteCharacter() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = format.getQuoteCharacter();
        CSVFormat result = format.withQuote(quoteChar);
        assertNotNull(result);
        // It may return the same instance if the quote character is the same
        // but since we don't have source of withQuote(Character), we just check equality
        assertEquals(quoteChar, result.getQuoteCharacter());
    }
}