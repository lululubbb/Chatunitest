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

public class CSVFormat_12_6Test {

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        // Set the field on the target instance
        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderWhenHeaderIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("a", "b"); // create a non-null header instance
        // Using reflection to set header to null explicitly to test null branch
        CSVFormat modifiableFormat = makeModifiable(format);
        setFinalField(modifiableFormat, "header", null);

        String[] header = modifiableFormat.getHeader();
        assertNull(header, "Header should be null when header field is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsCloneNotSameInstance() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("col1", "col2"); // create a non-null header instance
        // Using reflection to set header to a non-null array
        String[] originalHeader = new String[]{"col1", "col2"};
        CSVFormat modifiableFormat = makeModifiable(format);
        setFinalField(modifiableFormat, "header", originalHeader);

        String[] headerClone = modifiableFormat.getHeader();
        assertNotNull(headerClone, "Header clone should not be null");
        assertArrayEquals(originalHeader, headerClone, "Header clone should have same contents as original");
        assertNotSame(originalHeader, headerClone, "Header clone should not be the same instance as original");
    }

    /**
     * Creates a new instance of CSVFormat copying all fields from the given instance.
     * This allows modifying final fields safely without affecting the original immutable instance.
     */
    private CSVFormat makeModifiable(CSVFormat original) throws Exception {
        Field[] fields = CSVFormat.class.getDeclaredFields();
        CSVFormat copy = (CSVFormat) CSVFormat.class.getDeclaredConstructors()[0].newInstance(
                // Provide all constructor parameters by extracting from original via reflection
                getFieldValue(original, "delimiter"),
                getFieldValue(original, "quoteCharacter"),
                getFieldValue(original, "quoteMode"),
                getFieldValue(original, "commentMarker"),
                getFieldValue(original, "escapeCharacter"),
                getFieldValue(original, "ignoreSurroundingSpaces"),
                getFieldValue(original, "ignoreEmptyLines"),
                getFieldValue(original, "recordSeparator"),
                getFieldValue(original, "nullString"),
                getFieldValue(original, "headerComments"),
                getFieldValue(original, "header"),
                getFieldValue(original, "skipHeaderRecord"),
                getFieldValue(original, "allowMissingColumnNames"),
                getFieldValue(original, "ignoreHeaderCase"),
                getFieldValue(original, "trim"),
                getFieldValue(original, "trailingDelimiter")
        );
        return copy;
    }

    private Object getFieldValue(CSVFormat instance, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
}