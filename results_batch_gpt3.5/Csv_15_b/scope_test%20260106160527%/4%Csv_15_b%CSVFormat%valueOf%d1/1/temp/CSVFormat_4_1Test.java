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

class CSVFormat_4_1Test {

    @Test
    @Timeout(8000)
    void testValueOfWithValidPredefinedFormats() throws Exception {
        // Use reflection to access the enum CSVFormat.Predefined and its constants
        Class<?> predefinedClass = Class.forName("org.apache.commons.csv.CSVFormat$Predefined");

        @SuppressWarnings("unchecked")
        Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) predefinedClass;

        Object defaultEnum = Enum.valueOf(enumClass, "DEFAULT");
        Object excelEnum = Enum.valueOf(enumClass, "EXCEL");
        Object informixUnloadEnum = Enum.valueOf(enumClass, "INFORMIX_UNLOAD");
        Object informixUnloadCsvEnum = Enum.valueOf(enumClass, "INFORMIX_UNLOAD_CSV");
        Object mysqlEnum = Enum.valueOf(enumClass, "MYSQL");
        Object postgresqlCsvEnum = Enum.valueOf(enumClass, "POSTGRESQL_CSV");
        Object postgresqlTextEnum = Enum.valueOf(enumClass, "POSTGRESQL_TEXT");
        Object rfc4180Enum = Enum.valueOf(enumClass, "RFC4180");
        Object tdfEnum = Enum.valueOf(enumClass, "TDF");

        // Call getFormat() method on each enum constant to get the CSVFormat instance
        Method getFormatMethod = predefinedClass.getMethod("getFormat");

        CSVFormat defaultFormat = (CSVFormat) getFormatMethod.invoke(defaultEnum);
        CSVFormat excelFormat = (CSVFormat) getFormatMethod.invoke(excelEnum);
        CSVFormat informixUnloadFormat = (CSVFormat) getFormatMethod.invoke(informixUnloadEnum);
        CSVFormat informixUnloadCsvFormat = (CSVFormat) getFormatMethod.invoke(informixUnloadCsvEnum);
        CSVFormat mysqlFormat = (CSVFormat) getFormatMethod.invoke(mysqlEnum);
        CSVFormat postgresqlCsvFormat = (CSVFormat) getFormatMethod.invoke(postgresqlCsvEnum);
        CSVFormat postgresqlTextFormat = (CSVFormat) getFormatMethod.invoke(postgresqlTextEnum);
        CSVFormat rfc4180Format = (CSVFormat) getFormatMethod.invoke(rfc4180Enum);
        CSVFormat tdfFormat = (CSVFormat) getFormatMethod.invoke(tdfEnum);

        assertSame(defaultFormat, CSVFormat.valueOf("DEFAULT"));
        assertSame(excelFormat, CSVFormat.valueOf("EXCEL"));
        assertSame(informixUnloadFormat, CSVFormat.valueOf("INFORMIX_UNLOAD"));
        assertSame(informixUnloadCsvFormat, CSVFormat.valueOf("INFORMIX_UNLOAD_CSV"));
        assertSame(mysqlFormat, CSVFormat.valueOf("MYSQL"));
        assertSame(postgresqlCsvFormat, CSVFormat.valueOf("POSTGRESQL_CSV"));
        assertSame(postgresqlTextFormat, CSVFormat.valueOf("POSTGRESQL_TEXT"));
        assertSame(rfc4180Format, CSVFormat.valueOf("RFC4180"));
        assertSame(tdfFormat, CSVFormat.valueOf("TDF"));
    }

    @Test
    @Timeout(8000)
    void testValueOfWithInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("NonExistentFormat"));
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }
}