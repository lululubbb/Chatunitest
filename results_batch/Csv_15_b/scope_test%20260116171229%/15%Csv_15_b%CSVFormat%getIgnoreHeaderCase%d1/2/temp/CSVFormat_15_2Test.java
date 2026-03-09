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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_15_2Test {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatCustom;

    @BeforeEach
    public void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;
        csvFormatCustom = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreHeaderCase_DefaultFalse() {
        assertFalse(csvFormatDefault.getIgnoreHeaderCase(), "DEFAULT should have ignoreHeaderCase false");
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreHeaderCase_CustomTrue() {
        assertTrue(csvFormatCustom.getIgnoreHeaderCase(), "Custom CSVFormat should have ignoreHeaderCase true");
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreHeaderCase_ReflectionSetTrue() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withIgnoreHeaderCase(false);
        Field field = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        field.setAccessible(true);

        // Remove final modifier if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.setBoolean(csvFormat, true);
        assertTrue(csvFormat.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testGetIgnoreHeaderCase_ReflectionSetFalse() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
        Field field = CSVFormat.class.getDeclaredField("ignoreHeaderCase");
        field.setAccessible(true);

        // Remove final modifier if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.setBoolean(csvFormat, false);
        assertFalse(csvFormat.getIgnoreHeaderCase());
    }
}