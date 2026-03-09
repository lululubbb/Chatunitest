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
import java.lang.reflect.Method;

class CSVFormat_4_2Test {

    @Test
    @Timeout(8000)
    void testValueOf_withPredefinedFormats() throws Exception {
        // Use reflection to get the enum class CSVFormat.Predefined and its values
        Class<?> predefinedClass = Class.forName("org.apache.commons.csv.CSVFormat$Predefined");
        Object defaultEnum = Enum.valueOf((Class<Enum>) predefinedClass, "DEFAULT");
        Object excelEnum = Enum.valueOf((Class<Enum>) predefinedClass, "EXCEL");
        Object informixUnloadEnum = Enum.valueOf((Class<Enum>) predefinedClass, "INFORMIX_UNLOAD");
        Object informixUnloadCsvEnum = Enum.valueOf((Class<Enum>) predefinedClass, "INFORMIX_UNLOAD_CSV");
        Object mysqlEnum = Enum.valueOf((Class<Enum>) predefinedClass, "MYSQL");
        Object postgresqlCsvEnum = Enum.valueOf((Class<Enum>) predefinedClass, "POSTGRESQL_CSV");
        Object postgresqlTextEnum = Enum.valueOf((Class<Enum>) predefinedClass, "POSTGRESQL_TEXT");
        Object rfc4180Enum = Enum.valueOf((Class<Enum>) predefinedClass, "RFC4180");
        Object tdfEnum = Enum.valueOf((Class<Enum>) predefinedClass, "TDF");

        // Get getFormat method from Predefined enum
        Method getFormatMethod = predefinedClass.getDeclaredMethod("getFormat");

        // Assert CSVFormat.valueOf returns the same instance as the predefined format's getFormat()
        assertSame(getFormatMethod.invoke(defaultEnum), CSVFormat.valueOf("DEFAULT"));
        assertSame(getFormatMethod.invoke(excelEnum), CSVFormat.valueOf("EXCEL"));
        assertSame(getFormatMethod.invoke(informixUnloadEnum), CSVFormat.valueOf("INFORMIX_UNLOAD"));
        assertSame(getFormatMethod.invoke(informixUnloadCsvEnum), CSVFormat.valueOf("INFORMIX_UNLOAD_CSV"));
        assertSame(getFormatMethod.invoke(mysqlEnum), CSVFormat.valueOf("MYSQL"));
        assertSame(getFormatMethod.invoke(postgresqlCsvEnum), CSVFormat.valueOf("POSTGRESQL_CSV"));
        assertSame(getFormatMethod.invoke(postgresqlTextEnum), CSVFormat.valueOf("POSTGRESQL_TEXT"));
        assertSame(getFormatMethod.invoke(rfc4180Enum), CSVFormat.valueOf("RFC4180"));
        assertSame(getFormatMethod.invoke(tdfEnum), CSVFormat.valueOf("TDF"));
    }

    @Test
    @Timeout(8000)
    void testValueOf_invalidFormat_throwsIllegalArgumentException() {
        // Should throw IllegalArgumentException for unknown format or empty string
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("unknownFormat"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));

        // Should throw NullPointerException for null input
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }
}