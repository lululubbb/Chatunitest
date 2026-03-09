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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Constructor;

public class CSVFormat_10_6Test {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatCustom;

    @BeforeEach
    public void setUp() throws Exception {
        csvFormatDefault = CSVFormat.DEFAULT;

        // Use reflection to access the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class,
                boolean.class, boolean.class);

        constructor.setAccessible(true);

        csvFormatCustom = constructor.newInstance(
                ',', null, null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false, false);
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_Default() {
        assertEquals(',', csvFormatDefault.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_CustomDelimiter() {
        CSVFormat customFormat = csvFormatDefault.withDelimiter('|');
        assertEquals('|', customFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_OtherPredefinedFormats() {
        assertEquals(',', CSVFormat.EXCEL.getDelimiter());
        assertEquals('|', CSVFormat.INFORMIX_UNLOAD.getDelimiter());
        assertEquals(',', CSVFormat.INFORMIX_UNLOAD_CSV.getDelimiter());
        assertEquals('\t', CSVFormat.MYSQL.getDelimiter());
        assertEquals(',', CSVFormat.POSTGRESQL_CSV.getDelimiter());
        assertEquals('\t', CSVFormat.POSTGRESQL_TEXT.getDelimiter());
        assertEquals(',', CSVFormat.RFC4180.getDelimiter());
        assertEquals('\t', CSVFormat.TDF.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_WithVariousDelimiters() {
        CSVFormat formatComma = CSVFormat.newFormat(',');
        assertEquals(',', formatComma.getDelimiter());

        CSVFormat formatPipe = CSVFormat.newFormat('|');
        assertEquals('|', formatPipe.getDelimiter());

        CSVFormat formatTab = CSVFormat.newFormat('\t');
        assertEquals('\t', formatTab.getDelimiter());
    }
}