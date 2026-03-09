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

import java.lang.reflect.Method;

public class CSVFormat_65_6Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_char() {
        CSVFormat format = CSVFormat.DEFAULT;
        char quoteChar = '\'';

        CSVFormat result = format.withQuote(quoteChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(quoteChar), result.getQuoteCharacter());
        // Original instance remains unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_Character_null() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Access public withQuote(Character) method directly
        Method withQuoteCharMethod = CSVFormat.class.getDeclaredMethod("withQuote", Character.class);
        withQuoteCharMethod.setAccessible(true);

        // Passing null Character should be handled correctly
        CSVFormat result = (CSVFormat) withQuoteCharMethod.invoke(format, (Object) null);

        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        // Original instance remains unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), format.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithQuote_Character_valid() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = Character.valueOf('\"');

        // Access public withQuote(Character) method directly
        Method withQuoteCharMethod = CSVFormat.class.getDeclaredMethod("withQuote", Character.class);
        withQuoteCharMethod.setAccessible(true);

        CSVFormat result = (CSVFormat) withQuoteCharMethod.invoke(format, quoteChar);

        assertNotNull(result);
        assertEquals(quoteChar, result.getQuoteCharacter());
        // Original instance remains unchanged
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), format.getQuoteCharacter());
    }
}