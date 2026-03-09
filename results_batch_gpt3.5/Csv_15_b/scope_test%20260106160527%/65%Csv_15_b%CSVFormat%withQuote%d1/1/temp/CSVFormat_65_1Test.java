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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class CSVFormat_65_1Test {

    @Test
    @Timeout(8000)
    void testWithQuote_char() {
        // Test with a normal quote character
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = format.withQuote('"');
        assertNotNull(newFormat);
        assertEquals(Character.valueOf('"'), newFormat.getQuoteCharacter());
        assertNotSame(format, newFormat);

        // Test with a different quote character
        newFormat = format.withQuote('\'');
        assertEquals(Character.valueOf('\''), newFormat.getQuoteCharacter());

        // Test with a control character as quote (valid char)
        newFormat = format.withQuote('\n');
        assertEquals(Character.valueOf('\n'), newFormat.getQuoteCharacter());

        // Test with the null character (char 0)
        newFormat = format.withQuote('\0');
        assertEquals(Character.valueOf('\0'), newFormat.getQuoteCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithQuote_Character_null() throws Exception {
        // Use reflection to invoke public withQuote(Character) method with null argument
        CSVFormat format = CSVFormat.DEFAULT;
        Method method = CSVFormat.class.getMethod("withQuote", Character.class);

        CSVFormat result = (CSVFormat) method.invoke(format, new Object[] { null });
        assertNotNull(result);
        assertNull(result.getQuoteCharacter());
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithQuote_Character_nonNull() throws Exception {
        // Use reflection to invoke public withQuote(Character) method with a non-null argument
        CSVFormat format = CSVFormat.DEFAULT;
        Method method = CSVFormat.class.getMethod("withQuote", Character.class);

        CSVFormat result = (CSVFormat) method.invoke(format, Character.valueOf('Q'));
        assertNotNull(result);
        assertEquals(Character.valueOf('Q'), result.getQuoteCharacter());
        assertNotSame(format, result);
    }
}