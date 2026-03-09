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

class CSVFormat_4_3Test {

    @Test
    @Timeout(8000)
    void testValueOf_withValidPredefinedFormats() throws Exception {
        // Use reflection to get the Predefined enum class inside CSVFormat
        Class<?> predefinedClass = null;
        for (Class<?> innerClass : CSVFormat.class.getDeclaredClasses()) {
            if ("Predefined".equals(innerClass.getSimpleName())) {
                predefinedClass = innerClass;
                break;
            }
        }
        assertNotNull(predefinedClass, "Predefined enum class not found");

        // Use reflection to get enum constants
        Object[] enumConstants = predefinedClass.getEnumConstants();
        assertNotNull(enumConstants);
        assertTrue(enumConstants.length > 0);

        // Use reflection to get the getFormat() method
        Method getFormatMethod = predefinedClass.getMethod("getFormat");

        // Check that CSVFormat.DEFAULT etc. are not null
        assertNotNull(CSVFormat.DEFAULT);
        assertNotNull(CSVFormat.RFC4180);
        assertNotNull(CSVFormat.EXCEL);
        assertNotNull(CSVFormat.TDF);
        assertNotNull(CSVFormat.MYSQL);

        // For each predefined enum constant, test CSVFormat.valueOf returns the same instance as getFormat()
        for (Object enumConst : enumConstants) {
            String name = ((Enum<?>) enumConst).name();
            CSVFormat expectedFormat = (CSVFormat) getFormatMethod.invoke(enumConst);
            CSVFormat actualFormat = CSVFormat.valueOf(name);
            assertSame(expectedFormat, actualFormat, "CSVFormat.valueOf(\"" + name + "\") did not return expected format");
        }
    }

    @Test
    @Timeout(8000)
    void testValueOf_withCaseInsensitive() {
        // The original CSVFormat.valueOf(String) is case-sensitive and will throw IllegalArgumentException on lowercase.
        // Lowercase should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("default"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("rfc4180"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("excel"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("tdf"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("mysql"));
    }

    @Test
    @Timeout(8000)
    void testValueOf_withInvalidFormat_throwsException() {
        // Expect IllegalArgumentException for unknown format name
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("UnknownFormat"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }
}