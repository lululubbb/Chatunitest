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
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_39_6Test {

    @Test
    @Timeout(8000)
    public void testToString() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Given
        char delimiter = ',';
        Character quoteChar = '"';
        Character commentMarker = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\r\n";
        String nullString = null;
        Object[] headerComments = new Object[]{"Header comment 1", "Header comment 2"};
        String[] header = new String[]{"Header1", "Header2"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = false;
        boolean ignoreHeaderCase = true;
        boolean trim = false;
        boolean trailingDelimiter = false;

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance(delimiter, quoteChar, null, commentMarker, escape, ignoreSurroundingSpaces,
                ignoreEmptyLines, recordSeparator, nullString, headerComments, header, skipHeaderRecord,
                allowMissingColumnNames, ignoreHeaderCase, trim, trailingDelimiter);

        // When
        String result = csvFormat.toString();

        // Then
        String expected = "Delimiter=<,> Escape=<\\> QuoteChar<\"> CommentStart=<#> RecordSeparator=<\r\n> SurroundingSpaces:ignored IgnoreHeaderCase:ignored SkipHeaderRecord:true HeaderComments:[Header comment 1, Header comment 2] Header:[Header1, Header2]";
        assertEquals(expected, result);
    }
}