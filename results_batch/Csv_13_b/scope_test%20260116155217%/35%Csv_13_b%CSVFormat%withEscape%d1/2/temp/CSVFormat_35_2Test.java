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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_35_2Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_char() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Invoke public withEscape(char) method
        CSVFormat result = format.withEscape('\\');
        assertNotNull(result);
        assertEquals(Character.valueOf('\\'), result.getEscapeCharacter());

        // Also test withEscape with a different char
        CSVFormat result2 = format.withEscape('\"');
        assertNotNull(result2);
        assertEquals(Character.valueOf('\"'), result2.getEscapeCharacter());

        // Test withEscape with the same escape character returns same instance
        CSVFormat sameEscapeFormat = result.withEscape('\\');
        assertSame(result, sameEscapeFormat);

        // Use reflection to invoke private withEscape(Character) method
        Method withEscapeCharMethod = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        withEscapeCharMethod.setAccessible(true);

        // Invoke private method with non-null Character
        CSVFormat privateResult = (CSVFormat) withEscapeCharMethod.invoke(format, Character.valueOf('\''));
        assertNotNull(privateResult);
        assertEquals(Character.valueOf('\''), privateResult.getEscapeCharacter());

        // Invoke private method with null Character (should return format unchanged)
        CSVFormat privateResultNull = (CSVFormat) withEscapeCharMethod.invoke(format, new Object[] { null });
        assertSame(format, privateResultNull);
    }
}