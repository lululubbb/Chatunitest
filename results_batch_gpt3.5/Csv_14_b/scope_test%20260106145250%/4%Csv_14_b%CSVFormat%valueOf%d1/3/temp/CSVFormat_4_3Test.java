package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Predefined;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_4_3Test {

    @Test
    @Timeout(8000)
    public void testValueOf_withValidPredefinedFormat() throws Exception {
        // Get the enum constant DEFAULT
        Predefined defaultEnum = Predefined.valueOf("DEFAULT");

        // Use reflection to change the private final field 'format' inside the enum constant
        Field formatField = Predefined.class.getDeclaredField("format");
        formatField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(formatField, formatField.getModifiers() & ~Modifier.FINAL);

        CSVFormat expectedFormat = CSVFormat.DEFAULT;

        // Set the format field of DEFAULT enum constant to expectedFormat
        formatField.set(defaultEnum, expectedFormat);

        // Now test CSVFormat.valueOf returns expectedFormat
        CSVFormat actual = CSVFormat.valueOf("DEFAULT");
        assertNotNull(actual);
        assertEquals(expectedFormat, actual);
    }

    @Test
    @Timeout(8000)
    public void testValueOf_withNullFormatString() {
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }

    @Test
    @Timeout(8000)
    public void testValueOf_withUnknownFormatString() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("UNKNOWN"));
    }
}