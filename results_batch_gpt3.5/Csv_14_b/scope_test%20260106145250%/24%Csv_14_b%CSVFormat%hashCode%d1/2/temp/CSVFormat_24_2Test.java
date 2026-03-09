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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CSVFormat_24_2Test {

    @Test
    @Timeout(8000)
    public void testHashCode_DefaultInstances() {
        // Test hashCode for DEFAULT instance
        int defaultHashCode = CSVFormat.DEFAULT.hashCode();
        assertNotEquals(0, defaultHashCode);

        // Test hashCode for EXCEL instance
        int excelHashCode = CSVFormat.EXCEL.hashCode();
        assertNotEquals(0, excelHashCode);
        assertNotEquals(defaultHashCode, excelHashCode);

        // Test hashCode for INFORMIX_UNLOAD instance
        int informixUnloadHashCode = CSVFormat.INFORMIX_UNLOAD.hashCode();
        assertNotEquals(0, informixUnloadHashCode);
        assertNotEquals(defaultHashCode, informixUnloadHashCode);

        // Test hashCode for INFORMIX_UNLOAD_CSV instance
        int informixUnloadCsvHashCode = CSVFormat.INFORMIX_UNLOAD_CSV.hashCode();
        assertNotEquals(0, informixUnloadCsvHashCode);
        assertNotEquals(defaultHashCode, informixUnloadCsvHashCode);

        // Test hashCode for MYSQL instance
        int mysqlHashCode = CSVFormat.MYSQL.hashCode();
        assertNotEquals(0, mysqlHashCode);
        assertNotEquals(defaultHashCode, mysqlHashCode);

        // Test hashCode for RFC4180 instance
        int rfc4180HashCode = CSVFormat.RFC4180.hashCode();
        assertNotEquals(0, rfc4180HashCode);
        assertNotEquals(defaultHashCode, rfc4180HashCode);

        // Test hashCode for TDF instance
        int tdfHashCode = CSVFormat.TDF.hashCode();
        assertNotEquals(0, tdfHashCode);
        assertNotEquals(defaultHashCode, tdfHashCode);
    }

    @Test
    @Timeout(8000)
    public void testHashCode_CustomInstance() throws Exception {
        // Create CSVFormat instance with reflection to access private constructor
        Class<CSVFormat> clazz = CSVFormat.class;
        java.lang.reflect.Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        String[] header = new String[] {"col1", "col2"};
        Object[] headerComments = new Object[] {"comment1"};

        CSVFormat csvFormat = constructor.newInstance(
                ';', // delimiter
                '"', // quoteChar
                QuoteMode.ALL, // quoteMode
                '#', // commentStart
                '\\', // escape
                true, // ignoreSurroundingSpaces
                true, // ignoreEmptyLines
                "\n", // recordSeparator
                "NULL", // nullString
                headerComments, // headerComments
                header, // header
                true, // skipHeaderRecord
                true, // allowMissingColumnNames
                true, // ignoreHeaderCase
                true, // trim
                true // trailingDelimiter
        );

        int hash1 = csvFormat.hashCode();
        int hash2 = csvFormat.hashCode();
        assertEquals(hash1, hash2);

        // Change a field by creating another instance with a different delimiter
        CSVFormat csvFormatDiff = constructor.newInstance(
                ',', // delimiter changed
                '"', // quoteChar
                QuoteMode.ALL,
                '#',
                '\\',
                true,
                true,
                "\n",
                "NULL",
                headerComments,
                header,
                true,
                true,
                true,
                true,
                true
        );
        int hashDiff = csvFormatDiff.hashCode();
        assertNotEquals(hash1, hashDiff);
    }

    @Test
    @Timeout(8000)
    public void testHashCode_NullFields() throws Exception {
        // Create CSVFormat instance with null quoteMode, quoteCharacter, commentMarker, escapeCharacter, nullString, recordSeparator, header
        Class<CSVFormat> clazz = CSVFormat.class;
        java.lang.reflect.Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat = constructor.newInstance(
                ',', // delimiter
                null, // quoteChar null
                null, // quoteMode null
                null, // commentStart null
                null, // escape null
                false, // ignoreSurroundingSpaces
                false, // ignoreEmptyLines
                null, // recordSeparator null
                null, // nullString null
                null, // headerComments null
                null, // header null
                false, // skipHeaderRecord
                false, // allowMissingColumnNames
                false, // ignoreHeaderCase
                false, // trim
                false // trailingDelimiter
        );

        int hash = csvFormat.hashCode();
        assertNotEquals(0, hash);
    }

    @Test
    @Timeout(8000)
    public void testHashCode_ConsistencyWithEquals() throws Exception {
        // Create two equal CSVFormat instances and verify that their hashCodes are equal
        Class<CSVFormat> clazz = CSVFormat.class;
        java.lang.reflect.Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class,
                String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        String[] header = new String[] {"a", "b"};
        Object[] headerComments = new Object[] {"comm"};

        CSVFormat csvFormat1 = constructor.newInstance(
                ',', '"', QuoteMode.MINIMAL, '#', '\\', true, true, "\r\n", "NULL",
                headerComments, header, true, true, true, true, true);

        CSVFormat csvFormat2 = constructor.newInstance(
                ',', '"', QuoteMode.MINIMAL, '#', '\\', true, true, "\r\n", "NULL",
                headerComments, header, true, true, true, true, true);

        assertEquals(csvFormat1, csvFormat2);
        assertEquals(csvFormat1.hashCode(), csvFormat2.hashCode());
    }
}