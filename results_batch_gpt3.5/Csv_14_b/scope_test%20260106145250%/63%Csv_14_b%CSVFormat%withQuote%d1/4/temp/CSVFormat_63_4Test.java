package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_63_4Test {

    @Test
    @Timeout(8000)
    void testWithQuote_char() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Normal character quote
        char quoteChar = '"';
        CSVFormat result = baseFormat.withQuote(quoteChar);
        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteCharacter());

        // Different quote char
        char quoteChar2 = '\'';
        CSVFormat result2 = baseFormat.withQuote(quoteChar2);
        assertNotNull(result2);
        assertEquals(quoteChar2, result2.getQuoteCharacter());

        // Edge char: control character
        char quoteChar3 = '\u0000';
        CSVFormat result3 = baseFormat.withQuote(quoteChar3);
        assertNotNull(result3);
        assertEquals(quoteChar3, result3.getQuoteCharacter());

        // Edge char: max char
        char quoteChar4 = Character.MAX_VALUE;
        CSVFormat result4 = baseFormat.withQuote(quoteChar4);
        assertNotNull(result4);
        assertEquals(quoteChar4, result4.getQuoteCharacter());

        // Check original instance is unchanged (immutability)
        assertNotEquals(baseFormat, result);
        assertEquals('"', baseFormat.getQuoteCharacter());
    }
}