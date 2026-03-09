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
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

public class CSVFormat_35_6Test {

    @Test
    @Timeout(8000)
    void testWithEscapeChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat result = format.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithEscapeCharacterNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        Method withEscapeCharMethod = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        withEscapeCharMethod.setAccessible(true);

        CSVFormat result = (CSVFormat) withEscapeCharMethod.invoke(format, new Object[] { null });

        assertNotNull(result);
        assertNull(result.getEscapeCharacter());
        assertNotSame(format, result);
    }
}