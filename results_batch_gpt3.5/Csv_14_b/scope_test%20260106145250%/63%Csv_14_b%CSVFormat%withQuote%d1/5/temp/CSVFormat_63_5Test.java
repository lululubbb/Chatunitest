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

import java.lang.reflect.Method;

public class CSVFormat_63_5Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_char() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Direct call to withQuote(char)
        char quoteChar = '\'';
        CSVFormat resultFormat = baseFormat.withQuote(quoteChar);

        // The resulting format should not be the same instance
        assertNotSame(baseFormat, resultFormat);

        // The quote character should be set correctly
        assertEquals(Character.valueOf(quoteChar), resultFormat.getQuoteCharacter());

        // Using reflection to invoke public withQuote(Character) method to confirm delegation
        Method withQuoteCharMethod = CSVFormat.class.getMethod("withQuote", Character.class);
        CSVFormat reflectedResult = (CSVFormat) withQuoteCharMethod.invoke(baseFormat, quoteChar);
        assertEquals(resultFormat, reflectedResult);

        // Test with quoteChar as double quote (standard)
        Character baseQuoteChar = baseFormat.getQuoteCharacter();
        if (baseQuoteChar != null) {
            CSVFormat doubleQuoteFormat = baseFormat.withQuote(baseQuoteChar.charValue());
            assertEquals(baseQuoteChar, doubleQuoteFormat.getQuoteCharacter());
        }

        // Test with quoteChar as null character (edge case)
        // Since char is primitive, cannot pass null, but can test with some unusual char
        char unusualChar = '\0';
        CSVFormat unusualFormat = baseFormat.withQuote(unusualChar);
        assertEquals(Character.valueOf(unusualChar), unusualFormat.getQuoteCharacter());
    }
}