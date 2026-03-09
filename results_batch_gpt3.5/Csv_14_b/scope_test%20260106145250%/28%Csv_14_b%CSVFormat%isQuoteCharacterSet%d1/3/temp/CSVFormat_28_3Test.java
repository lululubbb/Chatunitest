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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_28_3Test {

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSetWhenQuoteCharacterIsSet() {
        CSVFormat format = CSVFormat.DEFAULT;
        // By default, DEFAULT has quoteCharacter set to DOUBLE_QUOTE_CHAR
        assertTrue(format.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSetWhenQuoteCharacterIsNull() throws Exception {
        // Create a CSVFormat instance with quoteCharacter set to null using reflection
        CSVFormat format = CSVFormat.DEFAULT;

        Field quoteCharacterField = CSVFormat.class.getDeclaredField("quoteCharacter");
        quoteCharacterField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(quoteCharacterField, quoteCharacterField.getModifiers() & ~Modifier.FINAL);

        quoteCharacterField.set(format, null);
        assertFalse(format.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSetWithCustomInstanceWithNullQuote() {
        CSVFormat format = CSVFormat.MYSQL; // MYSQL has quoteCharacter null
        assertFalse(format.isQuoteCharacterSet());
    }

    @Test
    @Timeout(8000)
    public void testIsQuoteCharacterSetWithCustomInstanceWithNonNullQuote() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD; // quoteCharacter set to DOUBLE_QUOTE_CHAR
        assertTrue(format.isQuoteCharacterSet());
    }
}