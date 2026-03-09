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
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

public class CSVFormat_55_4Test {

    @Test
    @Timeout(8000)
    public void testWithHeaderComments() {
        // Given
        Object[] headerComments = {"Comment1", "Comment2"};
        char delimiter = ',';
        char quoteCharacter = '"';
        QuoteMode quoteMode = null;
        Character commentMarker = null;
        Character escapeCharacter = null;
        boolean ignoreSurroundingSpaces = false;
        boolean ignoreEmptyLines = true;
        String recordSeparator = "\r\n";
        String nullString = null;
        String[] header = null;
        boolean skipHeaderRecord = false;
        boolean allowMissingColumnNames = false;
        boolean ignoreHeaderCase = false;
        boolean trim = false;
        boolean trailingDelimiter = false;

        CSVFormat csvFormat = null;
        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                    Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                    boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
            constructor.setAccessible(true);
            csvFormat = constructor.newInstance(delimiter, quoteCharacter, quoteMode, commentMarker, escapeCharacter,
                    ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, headerComments, header,
                    skipHeaderRecord, allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // When
        CSVFormat result = csvFormat.withHeaderComments("NewComment");

        // Then
        assertNotNull(result);
        assertArrayEquals(new Object[]{"NewComment"}, result.getHeaderComments());
    }
}