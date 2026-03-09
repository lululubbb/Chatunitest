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

class CSVFormat_4_2Test {

    @Test
    @Timeout(8000)
    void testValueOfWithDefaultFormats() throws Exception {
        // Access CSVFormat.Predefined enum class
        Class<?> predefinedClass = Class.forName("org.apache.commons.csv.CSVFormat$Predefined");

        // Access enum constants from CSVFormat.Predefined
        Object defaultEnum = Enum.valueOf((Class<Enum>) predefinedClass, "DEFAULT");
        Object rfc4180Enum = Enum.valueOf((Class<Enum>) predefinedClass, "RFC4180");
        Object excelEnum = Enum.valueOf((Class<Enum>) predefinedClass, "EXCEL");
        Object tdfEnum = Enum.valueOf((Class<Enum>) predefinedClass, "TDF");
        Object mysqlEnum = Enum.valueOf((Class<Enum>) predefinedClass, "MYSQL");

        // Retrieve the getFormat() method from the enum
        java.lang.reflect.Method getFormatMethod = predefinedClass.getMethod("getFormat");

        CSVFormat defaultFormat = (CSVFormat) getFormatMethod.invoke(defaultEnum);
        CSVFormat rfc4180Format = (CSVFormat) getFormatMethod.invoke(rfc4180Enum);
        CSVFormat excelFormat = (CSVFormat) getFormatMethod.invoke(excelEnum);
        CSVFormat tdfFormat = (CSVFormat) getFormatMethod.invoke(tdfEnum);
        CSVFormat mysqlFormat = (CSVFormat) getFormatMethod.invoke(mysqlEnum);

        // Assert that CSVFormat.valueOf returns the same instances as enum getFormat()
        assertSame(defaultFormat, CSVFormat.valueOf("DEFAULT"));
        assertSame(rfc4180Format, CSVFormat.valueOf("RFC4180"));
        assertSame(excelFormat, CSVFormat.valueOf("EXCEL"));
        assertSame(tdfFormat, CSVFormat.valueOf("TDF"));
        assertSame(mysqlFormat, CSVFormat.valueOf("MYSQL"));
    }

    @Test
    @Timeout(8000)
    void testValueOfCaseInsensitive() {
        // The valueOf method is case sensitive, so these should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("default"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("rfc4180"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("excel"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("tdf"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("mysql"));
    }

    @Test
    @Timeout(8000)
    void testValueOfInvalidFormatThrows() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("invalidFormatName"));
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));
    }
}