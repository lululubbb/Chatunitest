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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_27_5Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setup() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSet_NullStringNotNull_ReturnsTrue() {
        // Given
        csvFormat = csvFormat.withNullString("\\N");

        // When
        boolean result = invokePrivateMethod("isNullStringSet");

        // Then
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testIsNullStringSet_NullStringNull_ReturnsFalse() {
        // Given
        csvFormat = csvFormat.withNullString(null);

        // When
        boolean result = invokePrivateMethod("isNullStringSet");

        // Then
        assertFalse(result);
    }

    private boolean invokePrivateMethod(String methodName) {
        try {
            Method method = CSVFormat.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return (boolean) method.invoke(csvFormat);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }
}