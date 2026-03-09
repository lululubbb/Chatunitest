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

public class CSVFormat_65_3Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_char() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Call withQuote(char) normally
        CSVFormat result = format.withQuote('\'');
        assertNotNull(result);
        assertEquals(Character.valueOf('\''), result.getQuoteCharacter());

        // Use reflection to invoke public withQuote(Character) method to ensure coverage
        Method withQuoteCharMethod = CSVFormat.class.getMethod("withQuote", Character.class);

        // invoke withQuote(Character) with null
        CSVFormat resultNull = (CSVFormat) withQuoteCharMethod.invoke(format, (Object) null);
        assertNotNull(resultNull);
        assertNull(resultNull.getQuoteCharacter());

        // invoke withQuote(Character) with a character
        CSVFormat resultChar = (CSVFormat) withQuoteCharMethod.invoke(format, Character.valueOf('\"'));
        assertNotNull(resultChar);
        assertEquals(Character.valueOf('\"'), resultChar.getQuoteCharacter());
    }
}