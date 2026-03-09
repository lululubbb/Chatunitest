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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_20_4Test {

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator_DefaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\r\n", recordSeparator);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator_WithCustomRecordSeparatorString() {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator("\n");
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\n", recordSeparator);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator_WithCustomRecordSeparatorChar() {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator('\r');
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\r", recordSeparator);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator_UsingReflectionSetPrivateField() throws Exception {
        // Create a new CSVFormat instance by cloning DEFAULT with reflection
        CSVFormat format = CSVFormat.DEFAULT;

        // Access the private final field 'recordSeparator' in CSVFormat
        Field field = CSVFormat.class.getDeclaredField("recordSeparator");
        field.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // Set the recordSeparator field to a custom value
        field.set(format, "customSeparator");

        String recordSeparator = format.getRecordSeparator();
        assertEquals("customSeparator", recordSeparator);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator_ConstantFormats() {
        assertEquals("\n", CSVFormat.INFORMIX_UNLOAD.getRecordSeparator());
        assertEquals("\n", CSVFormat.INFORMIX_UNLOAD_CSV.getRecordSeparator());
        assertEquals("\n", CSVFormat.MYSQL.getRecordSeparator());
        assertEquals("\n", CSVFormat.POSTGRESQL_CSV.getRecordSeparator());
        assertEquals("\n", CSVFormat.POSTGRESQL_TEXT.getRecordSeparator());
        assertEquals("\r\n", CSVFormat.RFC4180.getRecordSeparator());
        assertEquals("\r\n", CSVFormat.TDF.getRecordSeparator());
    }
}