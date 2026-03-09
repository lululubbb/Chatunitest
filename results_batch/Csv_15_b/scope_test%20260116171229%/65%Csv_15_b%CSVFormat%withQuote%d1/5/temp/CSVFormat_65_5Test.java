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

public class CSVFormat_65_5Test {

    @Test
    @Timeout(8000)
    public void testWithQuote_char() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Invoke public withQuote(char) method
        CSVFormat result = format.withQuote('"');
        assertNotNull(result);
        assertEquals(Character.valueOf('"'), result.getQuoteCharacter());

        // Also test withQuote(char) with null character (edge case)
        result = format.withQuote('\0');
        assertNotNull(result);
        assertEquals(Character.valueOf('\0'), result.getQuoteCharacter());

        // Use reflection to invoke public withQuote(Character) method
        Method withQuoteCharacterMethod = CSVFormat.class.getMethod("withQuote", Character.class);

        // Call withQuote(Character) with a non-null character
        CSVFormat result2 = (CSVFormat) withQuoteCharacterMethod.invoke(format, Character.valueOf('\''));
        assertNotNull(result2);
        assertEquals(Character.valueOf('\''), result2.getQuoteCharacter());

        // Call withQuote(Character) with null
        CSVFormat result3 = (CSVFormat) withQuoteCharacterMethod.invoke(format, (Object) null);
        assertNotNull(result3);
        assertNull(result3.getQuoteCharacter());
    }
}