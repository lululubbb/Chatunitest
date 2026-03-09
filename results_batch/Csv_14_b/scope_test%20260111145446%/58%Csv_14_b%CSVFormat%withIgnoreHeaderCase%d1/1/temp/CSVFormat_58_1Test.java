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

import java.io.StringReader;
import org.junit.jupiter.api.Test;

public class CSVFormat_58_1Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withIgnoreHeaderCase();

        // Then
        assertTrue(result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase_false() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withIgnoreHeaderCase(false);

        // Then
        assertFalse(result.getIgnoreHeaderCase());
    }

    // Additional test cases for branch coverage can be added here

    // Helper method to invoke private methods using reflection
    private Object invokePrivateMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object... args) {
        try {
            java.lang.reflect.Method method = obj.getClass().getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method.invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}