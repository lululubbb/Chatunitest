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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_63_1Test {

    @Test
    @Timeout(8000)
    void testWithQuote_char() {
        // Use a normal quote character
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withQuote(Character.valueOf('"'));
        assertNotNull(result);
        assertEquals(Character.valueOf('"'), result.getQuoteCharacter());

        // Use a different quote character
        result = format.withQuote(Character.valueOf('\''));
        assertNotNull(result);
        assertEquals(Character.valueOf('\''), result.getQuoteCharacter());

        // Use a special character as quote
        result = format.withQuote(Character.valueOf('\u0000'));
        assertNotNull(result);
        assertEquals(Character.valueOf('\u0000'), result.getQuoteCharacter());

        // Use a newline character as quote (unusual but valid char)
        result = format.withQuote(Character.valueOf('\n'));
        assertNotNull(result);
        assertEquals(Character.valueOf('\n'), result.getQuoteCharacter());
    }
}