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

public class CSVFormat_63_6Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_char() {
        // Arrange
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Act
        CSVFormat result = baseFormat.withQuote('\''); // single quote char as primitive char

        // Assert
        assertNotNull(result);
        assertNotSame(baseFormat, result);
        assertEquals(Character.valueOf('\''), result.getQuoteCharacter());

        // Also test with double quote char to verify it matches DEFAULT's quote char
        CSVFormat doubleQuoteFormat = baseFormat.withQuote('"');
        assertEquals(Character.valueOf('"'), doubleQuoteFormat.getQuoteCharacter());

        // Test with a non-printable character as quote char
        CSVFormat nonPrintableQuote = baseFormat.withQuote('\u0000');
        assertEquals(Character.valueOf('\u0000'), nonPrintableQuote.getQuoteCharacter());
    }
}