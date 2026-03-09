package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_2_2Test {

    @Test
    @Timeout(8000)
    public void testIsLineBreak() throws Exception {
        // Given
        char lineBreakChar = '\n';
        
        // When
        boolean result = invokePrivateStaticMethod(CSVFormat.class, "isLineBreak", lineBreakChar);
        
        // Then
        assertTrue(result);
    }

    private static boolean invokePrivateStaticMethod(Class<?> clazz, String methodName, Object... args)
            throws Exception {
        Method method = clazz.getDeclaredMethod(methodName, char.class);
        method.setAccessible(true);
        return (boolean) method.invoke(null, args);
    }
}