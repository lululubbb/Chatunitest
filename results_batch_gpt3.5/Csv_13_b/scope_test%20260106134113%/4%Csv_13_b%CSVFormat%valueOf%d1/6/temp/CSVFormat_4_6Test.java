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

import java.lang.reflect.Field;

class CSVFormat_4_6Test {

    @Test
    @Timeout(8000)
    void testValueOf_withValidPredefinedFormats() throws Exception {
        // Use reflection to set the Predefined enum class inside CSVFormat
        Class<?> predefinedClass = null;
        for (Class<?> innerClass : CSVFormat.class.getDeclaredClasses()) {
            if ("Predefined".equals(innerClass.getSimpleName())) {
                predefinedClass = innerClass;
                break;
            }
        }
        assertNotNull(predefinedClass, "Predefined enum class not found");

        // Validate that the enum constants exist
        Object[] enumConstants = predefinedClass.getEnumConstants();
        assertNotNull(enumConstants);
        assertTrue(enumConstants.length > 0);

        // Test DEFAULT
        CSVFormat formatDefault = CSVFormat.valueOf("DEFAULT");
        assertNotNull(formatDefault);
        assertEquals(CSVFormat.DEFAULT.getDelimiter(), formatDefault.getDelimiter());

        // Test RFC4180
        CSVFormat formatRFC4180 = CSVFormat.valueOf("RFC4180");
        assertNotNull(formatRFC4180);
        assertEquals(CSVFormat.RFC4180.getDelimiter(), formatRFC4180.getDelimiter());

        // Test EXCEL
        CSVFormat formatExcel = CSVFormat.valueOf("EXCEL");
        assertNotNull(formatExcel);
        assertEquals(CSVFormat.EXCEL.getDelimiter(), formatExcel.getDelimiter());

        // Test TDF
        CSVFormat formatTdf = CSVFormat.valueOf("TDF");
        assertNotNull(formatTdf);
        assertEquals(CSVFormat.TDF.getDelimiter(), formatTdf.getDelimiter());

        // Test MYSQL
        CSVFormat formatMysql = CSVFormat.valueOf("MYSQL");
        assertNotNull(formatMysql);
        assertEquals(CSVFormat.MYSQL.getDelimiter(), formatMysql.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testValueOf_withInvalidFormat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("InvalidFormatName"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }
}