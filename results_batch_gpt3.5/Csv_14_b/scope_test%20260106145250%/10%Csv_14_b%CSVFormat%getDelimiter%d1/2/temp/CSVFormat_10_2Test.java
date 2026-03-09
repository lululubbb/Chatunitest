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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

public class CSVFormat_10_2Test {

    @Test
    @Timeout(8000)
    void testGetDelimiter_DefaultConstructor() throws Exception {
        Class<?> quoteModeClass = Class.forName("org.apache.commons.csv.QuoteMode");
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, quoteModeClass, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class,
                Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(',', null, null, null, null,
                false, true, "\r\n", null, null, null,
                false, false, false, false);
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_PredefinedConstants() {
        assertEquals(',', CSVFormat.DEFAULT.getDelimiter());
        assertEquals(',', CSVFormat.EXCEL.getDelimiter());
        assertEquals('|', CSVFormat.INFORMIX_UNLOAD.getDelimiter());
        assertEquals(',', CSVFormat.INFORMIX_UNLOAD_CSV.getDelimiter());
        assertEquals('\t', CSVFormat.MYSQL.getDelimiter());
        assertEquals(',', CSVFormat.RFC4180.getDelimiter());
        assertEquals('\t', CSVFormat.TDF.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_WithDelimiterMethod() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(';');
        assertEquals(';', format.getDelimiter());

        CSVFormat format2 = CSVFormat.DEFAULT.withDelimiter('\t');
        assertEquals('\t', format2.getDelimiter());
    }
}