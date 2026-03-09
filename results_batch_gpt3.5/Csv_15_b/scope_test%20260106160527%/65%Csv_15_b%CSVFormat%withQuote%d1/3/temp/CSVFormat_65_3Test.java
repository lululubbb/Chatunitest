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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Method;

public class CSVFormat_65_3Test {

    private CSVFormat csvFormatDefault;

    @BeforeEach
    public void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_char() {
        // Test with normal quote char
        char quoteChar = '"';
        CSVFormat result = csvFormatDefault.withQuote(quoteChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());

        // Test with different quote char
        quoteChar = '\'';
        result = csvFormatDefault.withQuote(quoteChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());

        // Test with quote char that is same as delimiter (comma)
        quoteChar = ',';
        result = csvFormatDefault.withQuote(quoteChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());

        // Test with quote char that is line break (CR)
        quoteChar = '\r';
        result = csvFormatDefault.withQuote(quoteChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());

        // Test with quote char that is line break (LF)
        quoteChar = '\n';
        result = csvFormatDefault.withQuote(quoteChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_Character_null() throws Exception {
        // Access public withQuote(Character) method via reflection
        Method withQuoteCharMethod = CSVFormat.class.getMethod("withQuote", Character.class);

        // Pass null Character
        CSVFormat result = (CSVFormat) withQuoteCharMethod.invoke(csvFormatDefault, (Object) null);
        assertNotNull(result);
        assertNull(result.getQuoteCharacter());

        // Pass a valid Character
        Character quoteChar = Character.valueOf('\'');
        result = (CSVFormat) withQuoteCharMethod.invoke(csvFormatDefault, quoteChar);
        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteCharacter());
    }
}