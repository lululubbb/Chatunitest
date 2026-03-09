package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.StringReader;
import org.junit.jupiter.api.Test;

public class CSVFormat_1_5Test {

    @Test
    @Timeout(8000)
    public void testIsLineBreak() throws Exception {
        assertTrue(invokeIsLineBreak('a') == false);
        assertTrue(invokeIsLineBreak('\n') == true);
        assertTrue(invokeIsLineBreak('\r') == true);
    }

    private boolean invokeIsLineBreak(char c) throws Exception {
        return (boolean) invokePrivateMethod(CSVFormat.class, "isLineBreak", char.class, c);
    }

    private Object invokePrivateMethod(Class<?> clazz, String methodName, Class<?> parameterType, Object argument) throws Exception {
        java.lang.reflect.Method method = clazz.getDeclaredMethod(methodName, parameterType);
        method.setAccessible(true);
        return method.invoke(null, argument);
    }
}