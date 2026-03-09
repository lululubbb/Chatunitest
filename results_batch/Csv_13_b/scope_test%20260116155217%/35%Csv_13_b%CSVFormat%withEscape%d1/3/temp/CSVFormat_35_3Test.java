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

public class CSVFormat_35_3Test {

    @Test
    @Timeout(8000)
    public void testWithEscapeChar() {
        // Arrange
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        // Act
        CSVFormat result = baseFormat.withEscape(escapeChar);

        // Assert
        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeCharUsingReflection() throws Exception {
        // Arrange
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char escapeChar = 'e';

        // Use reflection to invoke public method withEscape(Character)
        Method withEscapeCharacterMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        // Act
        CSVFormat result = (CSVFormat) withEscapeCharacterMethod.invoke(baseFormat, Character.valueOf(escapeChar));

        // Assert
        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
    }
}