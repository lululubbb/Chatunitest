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

class CSVFormat_63_3Test {

    @Test
    @Timeout(8000)
    void testWithQuote_withChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with a regular quote character
        CSVFormat formatWithQuote = baseFormat.withQuote('"');
        assertNotNull(formatWithQuote);
        assertEquals(Character.valueOf('"'), formatWithQuote.getQuoteCharacter());

        // Test with a different quote character
        CSVFormat formatWithSingleQuote = baseFormat.withQuote('\'');
        assertNotNull(formatWithSingleQuote);
        assertEquals(Character.valueOf('\''), formatWithSingleQuote.getQuoteCharacter());

        // Test with the default quote character unchanged
        Character defaultQuote = CSVFormat.DEFAULT.getQuoteCharacter();
        char quoteChar = defaultQuote != null ? defaultQuote : '\0';
        CSVFormat formatWithSameQuote = baseFormat.withQuote(quoteChar);
        assertNotNull(formatWithSameQuote);
        assertEquals(defaultQuote, formatWithSameQuote.getQuoteCharacter());

        // Test with a non-printable char as quote (edge case)
        CSVFormat formatWithNonPrintable = baseFormat.withQuote('\0');
        assertNotNull(formatWithNonPrintable);
        assertEquals(Character.valueOf('\0'), formatWithNonPrintable.getQuoteCharacter());
    }
}