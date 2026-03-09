package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

public class CSVFormat_35_2Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_char() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Call withEscape(char) directly
        CSVFormat result = format.withEscape('\\');

        // Verify that the returned CSVFormat has the escape character set to '\\'
        assertNotNull(result);
        assertEquals(Character.valueOf('\\'), result.getEscapeCharacter());

        // Also test with a different escape character
        CSVFormat result2 = format.withEscape('E');
        assertNotNull(result2);
        assertEquals(Character.valueOf('E'), result2.getEscapeCharacter());

        // Test withEscape(char) with the default escape character (e.g., '\0')
        CSVFormat result3 = format.withEscape('\0');
        assertNotNull(result3);
        assertEquals(Character.valueOf('\0'), result3.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_Character_privateMethodInvocation() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to access the public withEscape(Character) method
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        // Invoke withEscape(Character) with a non-null character
        CSVFormat result = (CSVFormat) withEscapeCharMethod.invoke(format, Character.valueOf('Q'));
        assertNotNull(result);
        assertEquals(Character.valueOf('Q'), result.getEscapeCharacter());

        // Invoke withEscape(Character) with null (should return a format with null escapeCharacter)
        CSVFormat resultNull = (CSVFormat) withEscapeCharMethod.invoke(format, new Object[] { null });
        assertNotNull(resultNull);
        assertNull(resultNull.getEscapeCharacter());
    }
}