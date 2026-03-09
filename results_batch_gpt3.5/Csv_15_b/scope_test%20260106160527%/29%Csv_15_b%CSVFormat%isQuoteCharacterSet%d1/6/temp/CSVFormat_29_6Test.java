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

class CSVFormat_29_6Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        // Use a mutable instance by creating a new CSVFormat with desired initial state
        csvFormat = CSVFormat.DEFAULT.withQuote('\"');
    }

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_WhenQuoteCharacterIsSet() throws Exception {
        // Create a new CSVFormat instance with quoteCharacter set to '"'
        CSVFormat format = CSVFormat.DEFAULT.withQuote('\"');
        setFinalField(format, "quoteCharacter", Character.valueOf('\"'));
        assertTrue(format.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_WhenQuoteCharacterIsNull() throws Exception {
        // Create a new CSVFormat instance with quoteCharacter set to null
        CSVFormat format = CSVFormat.DEFAULT.withQuote((Character) null);
        setFinalField(format, "quoteCharacter", null);
        assertFalse(format.isQuoteCharacterSet());
    }
}