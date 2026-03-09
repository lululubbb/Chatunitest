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

class CSVFormat_4_4Test {

    @Test
    @Timeout(8000)
    void testValueOf_withDefaultFormats() {
        // Test known predefined format names with exact enum names (uppercase)
        CSVFormat formatDefault = CSVFormat.valueOf("DEFAULT");
        assertNotNull(formatDefault);
        assertEquals(CSVFormat.DEFAULT.getDelimiter(), formatDefault.getDelimiter());

        CSVFormat formatRFC4180 = CSVFormat.valueOf("RFC4180");
        assertNotNull(formatRFC4180);
        assertEquals(CSVFormat.RFC4180.getDelimiter(), formatRFC4180.getDelimiter());

        CSVFormat formatExcel = CSVFormat.valueOf("EXCEL");
        assertNotNull(formatExcel);
        assertEquals(CSVFormat.EXCEL.getDelimiter(), formatExcel.getDelimiter());

        CSVFormat formatTDF = CSVFormat.valueOf("TDF");
        assertNotNull(formatTDF);
        assertEquals(CSVFormat.TDF.getDelimiter(), formatTDF.getDelimiter());

        CSVFormat formatMySQL = CSVFormat.valueOf("MYSQL");
        assertNotNull(formatMySQL);
        assertEquals(CSVFormat.MYSQL.getDelimiter(), formatMySQL.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testValueOf_caseInsensitive() throws Exception {
        // The CSVFormat.valueOf(String) method is case-sensitive.
        // Using reflection to invoke the enum valueOf method directly (case-sensitive) on Predefined enum.
        Class<?> predefinedEnum = Class.forName("org.apache.commons.csv.CSVFormat$Predefined");
        Method enumValueOf = predefinedEnum.getMethod("valueOf", String.class);

        // Uppercase should succeed
        Object predefinedDefaultUpper = enumValueOf.invoke(null, "DEFAULT");
        assertNotNull(predefinedDefaultUpper);

        // Lowercase should throw IllegalArgumentException wrapped in InvocationTargetException
        assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
            enumValueOf.invoke(null, "default");
        });

        // Also test CSVFormat.valueOf itself
        assertEquals(CSVFormat.DEFAULT, CSVFormat.valueOf("DEFAULT"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("default"));
    }

    @Test
    @Timeout(8000)
    void testValueOf_invalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("InvalidFormatName"));
    }

}