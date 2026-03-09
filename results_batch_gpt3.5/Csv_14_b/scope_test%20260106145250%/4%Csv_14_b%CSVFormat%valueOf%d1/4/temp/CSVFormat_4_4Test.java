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

class CSVFormatValueOfTest {

    @Test
    @Timeout(8000)
    void testValueOf_withValidPredefinedFormats() throws Exception {
        // Use reflection to access the nested enum Predefined inside CSVFormat
        Class<?> predefinedClass = Class.forName("org.apache.commons.csv.CSVFormat$Predefined");
        @SuppressWarnings("unchecked")
        Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) predefinedClass;

        Method valueOfMethod = Enum.class.getDeclaredMethod("valueOf", Class.class, String.class);

        Object formatDefaultEnum = valueOfMethod.invoke(null, enumClass, "DEFAULT");
        Object formatDefault = predefinedClass.getMethod("getFormat").invoke(formatDefaultEnum);
        assertNotNull(formatDefault);
        assertEquals(CSVFormat.DEFAULT.getDelimiter(), ((CSVFormat) formatDefault).getDelimiter());

        Object formatExcelEnum = valueOfMethod.invoke(null, enumClass, "EXCEL");
        Object formatExcel = predefinedClass.getMethod("getFormat").invoke(formatExcelEnum);
        assertNotNull(formatExcel);
        assertEquals(CSVFormat.EXCEL.getDelimiter(), ((CSVFormat) formatExcel).getDelimiter());

        Object formatInformixUnloadEnum = valueOfMethod.invoke(null, enumClass, "INFORMIX_UNLOAD");
        Object formatInformixUnload = predefinedClass.getMethod("getFormat").invoke(formatInformixUnloadEnum);
        assertNotNull(formatInformixUnload);
        assertEquals(CSVFormat.INFORMIX_UNLOAD.getDelimiter(), ((CSVFormat) formatInformixUnload).getDelimiter());

        Object formatMysqlEnum = valueOfMethod.invoke(null, enumClass, "MYSQL");
        Object formatMysql = predefinedClass.getMethod("getFormat").invoke(formatMysqlEnum);
        assertNotNull(formatMysql);
        assertEquals(CSVFormat.MYSQL.getDelimiter(), ((CSVFormat) formatMysql).getDelimiter());

        Object formatRfc4180Enum = valueOfMethod.invoke(null, enumClass, "RFC4180");
        Object formatRfc4180 = predefinedClass.getMethod("getFormat").invoke(formatRfc4180Enum);
        assertNotNull(formatRfc4180);
        assertEquals(CSVFormat.RFC4180.getDelimiter(), ((CSVFormat) formatRfc4180).getDelimiter());

        Object formatTdfEnum = valueOfMethod.invoke(null, enumClass, "TDF");
        Object formatTdf = predefinedClass.getMethod("getFormat").invoke(formatTdfEnum);
        assertNotNull(formatTdf);
        assertEquals(CSVFormat.TDF.getDelimiter(), ((CSVFormat) formatTdf).getDelimiter());

        // Now test CSVFormat.valueOf(String) directly
        CSVFormat valDefault = CSVFormat.valueOf("DEFAULT");
        assertNotNull(valDefault);
        assertEquals(CSVFormat.DEFAULT.getDelimiter(), valDefault.getDelimiter());

        CSVFormat valExcel = CSVFormat.valueOf("EXCEL");
        assertNotNull(valExcel);
        assertEquals(CSVFormat.EXCEL.getDelimiter(), valExcel.getDelimiter());

        CSVFormat valInformixUnload = CSVFormat.valueOf("INFORMIX_UNLOAD");
        assertNotNull(valInformixUnload);
        assertEquals(CSVFormat.INFORMIX_UNLOAD.getDelimiter(), valInformixUnload.getDelimiter());

        CSVFormat valMysql = CSVFormat.valueOf("MYSQL");
        assertNotNull(valMysql);
        assertEquals(CSVFormat.MYSQL.getDelimiter(), valMysql.getDelimiter());

        CSVFormat valRfc4180 = CSVFormat.valueOf("RFC4180");
        assertNotNull(valRfc4180);
        assertEquals(CSVFormat.RFC4180.getDelimiter(), valRfc4180.getDelimiter());

        CSVFormat valTdf = CSVFormat.valueOf("TDF");
        assertNotNull(valTdf);
        assertEquals(CSVFormat.TDF.getDelimiter(), valTdf.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testValueOf_withNullInput() {
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }

    @Test
    @Timeout(8000)
    void testValueOf_withInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("NonExistentFormat"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(" "));
    }
}