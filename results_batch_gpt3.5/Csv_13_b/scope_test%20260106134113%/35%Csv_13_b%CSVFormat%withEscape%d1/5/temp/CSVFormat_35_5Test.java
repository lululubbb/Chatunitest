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

class CSVFormat_35_5Test {

    @Test
    @Timeout(8000)
    void testWithEscapeChar() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Test withEscape(char) calls withEscape(Character)
        CSVFormat result = format.withEscape('\\');
        assertNotNull(result);
        assertEquals(Character.valueOf('\\'), result.getEscapeCharacter());

        // Test withEscape(Character) with null
        Method withEscapeCharMethod = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        withEscapeCharMethod.setAccessible(true);
        CSVFormat resultNull = (CSVFormat) withEscapeCharMethod.invoke(format, (Object) null);
        assertNotNull(resultNull);
        assertNull(resultNull.getEscapeCharacter());

        // Test withEscape(Character) with non-null character
        CSVFormat resultQuote = (CSVFormat) withEscapeCharMethod.invoke(format, Character.valueOf('Q'));
        assertNotNull(resultQuote);
        assertEquals(Character.valueOf('Q'), resultQuote.getEscapeCharacter());
    }
}