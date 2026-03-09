package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_4_6Test {

    @Test
    @Timeout(8000)
    void testValueOfKnownFormats() throws Exception {
        // Use reflection to access the enum CSVFormat.Predefined and test valueOf on it
        Class<?> predefinedClass = Class.forName("org.apache.commons.csv.CSVFormat$Predefined");

        Object formatDefault = Enum.valueOf((Class<Enum>) predefinedClass, "DEFAULT");
        assertNotNull(formatDefault);
        assertEquals(CSVFormat.DEFAULT.getDelimiter(),
                predefinedClass.getMethod("getFormat").invoke(formatDefault).getClass()
                        .getMethod("getDelimiter").invoke(predefinedClass.getMethod("getFormat").invoke(formatDefault)));

        Object formatExcel = Enum.valueOf((Class<Enum>) predefinedClass, "EXCEL");
        assertNotNull(formatExcel);
        assertEquals(CSVFormat.EXCEL.getDelimiter(),
                predefinedClass.getMethod("getFormat").invoke(formatExcel).getClass()
                        .getMethod("getDelimiter").invoke(predefinedClass.getMethod("getFormat").invoke(formatExcel)));

        Object formatInformixUnload = Enum.valueOf((Class<Enum>) predefinedClass, "INFORMIX_UNLOAD");
        assertNotNull(formatInformixUnload);
        assertEquals(CSVFormat.INFORMIX_UNLOAD.getDelimiter(),
                predefinedClass.getMethod("getFormat").invoke(formatInformixUnload).getClass()
                        .getMethod("getDelimiter").invoke(predefinedClass.getMethod("getFormat").invoke(formatInformixUnload)));

        Object formatMySQL = Enum.valueOf((Class<Enum>) predefinedClass, "MYSQL");
        assertNotNull(formatMySQL);
        assertEquals(CSVFormat.MYSQL.getDelimiter(),
                predefinedClass.getMethod("getFormat").invoke(formatMySQL).getClass()
                        .getMethod("getDelimiter").invoke(predefinedClass.getMethod("getFormat").invoke(formatMySQL)));

        Object formatPostgreSQLCSV = Enum.valueOf((Class<Enum>) predefinedClass, "POSTGRESQL_CSV");
        assertNotNull(formatPostgreSQLCSV);
        assertEquals(CSVFormat.POSTGRESQL_CSV.getDelimiter(),
                predefinedClass.getMethod("getFormat").invoke(formatPostgreSQLCSV).getClass()
                        .getMethod("getDelimiter").invoke(predefinedClass.getMethod("getFormat").invoke(formatPostgreSQLCSV)));

        Object formatPostgreSQLText = Enum.valueOf((Class<Enum>) predefinedClass, "POSTGRESQL_TEXT");
        assertNotNull(formatPostgreSQLText);
        assertEquals(CSVFormat.POSTGRESQL_TEXT.getDelimiter(),
                predefinedClass.getMethod("getFormat").invoke(formatPostgreSQLText).getClass()
                        .getMethod("getDelimiter").invoke(predefinedClass.getMethod("getFormat").invoke(formatPostgreSQLText)));

        Object formatRFC4180 = Enum.valueOf((Class<Enum>) predefinedClass, "RFC4180");
        assertNotNull(formatRFC4180);
        assertEquals(CSVFormat.RFC4180.getDelimiter(),
                predefinedClass.getMethod("getFormat").invoke(formatRFC4180).getClass()
                        .getMethod("getDelimiter").invoke(predefinedClass.getMethod("getFormat").invoke(formatRFC4180)));

        Object formatTDF = Enum.valueOf((Class<Enum>) predefinedClass, "TDF");
        assertNotNull(formatTDF);
        assertEquals(CSVFormat.TDF.getDelimiter(),
                predefinedClass.getMethod("getFormat").invoke(formatTDF).getClass()
                        .getMethod("getDelimiter").invoke(predefinedClass.getMethod("getFormat").invoke(formatTDF)));
    }

    @Test
    @Timeout(8000)
    void testValueOfCaseInsensitive() {
        // Test that case-insensitive names throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("default"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("dEfAuLt"));
    }

    @Test
    @Timeout(8000)
    void testValueOfInvalidFormat() {
        // Test that invalid format name throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("NonExistentFormat"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(null));
    }
}