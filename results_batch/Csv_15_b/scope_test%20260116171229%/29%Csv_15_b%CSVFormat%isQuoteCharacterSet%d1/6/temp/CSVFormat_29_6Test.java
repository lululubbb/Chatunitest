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

public class CSVFormat_29_6Test {

    private CSVFormat setQuoteCharacter(CSVFormat target, Character value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField("quoteCharacter");
        field.setAccessible(true);

        // Remove final modifier from field (only works on Java <= 11)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // Since CSVFormat is immutable, we need to create a new instance with the modified quoteCharacter
        // But here, we forcibly set the field on the existing instance for testing purposes.
        field.set(target, value);
        return target;
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuote(null);
        // Using reflection to set private final field quoteCharacter
        setQuoteCharacter(format, '\"');

        boolean result = format.isQuoteCharacterSet();
        assertTrue(result, "Expected quoteCharacter to be set");
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuote('\"');
        // Using reflection to set private final field quoteCharacter to null
        setQuoteCharacter(format, null);

        boolean result = format.isQuoteCharacterSet();
        assertFalse(result, "Expected quoteCharacter to be null");
    }
}