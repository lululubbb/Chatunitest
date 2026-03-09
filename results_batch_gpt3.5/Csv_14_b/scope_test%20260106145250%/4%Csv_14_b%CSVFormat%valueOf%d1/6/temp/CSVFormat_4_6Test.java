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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class CSVFormat_4_6Test {

    @Test
    @Timeout(8000)
    void testValueOf_knownFormats() throws Exception {
        // Use reflection to access the enum CSVFormat.Predefined
        Class<?> predefinedEnum = Class.forName("org.apache.commons.csv.CSVFormat$Predefined");

        // Get enum constants
        Object[] constants = predefinedEnum.getEnumConstants();

        // Get the getFormat method once
        Method getFormatMethod = predefinedEnum.getMethod("getFormat");

        for (Object constant : constants) {
            String name = ((Enum<?>) constant).name();
            CSVFormat format = CSVFormat.valueOf(name);
            assertNotNull(format);

            // Invoke getFormat() on enum constant to get expected CSVFormat
            CSVFormat expectedFormat = (CSVFormat) getFormatMethod.invoke(constant);

            assertEquals(expectedFormat.getDelimiter(), format.getDelimiter(), "Delimiter mismatch for format: " + name);
        }
    }

    @Test
    @Timeout(8000)
    void testValueOf_invalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("InvalidFormatName"));
    }

    @Test
    @Timeout(8000)
    void testValueOf_nullFormat() {
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }
}