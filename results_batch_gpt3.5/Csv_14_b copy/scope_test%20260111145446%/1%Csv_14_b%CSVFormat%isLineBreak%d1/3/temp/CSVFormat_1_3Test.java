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
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_1_3Test {

    @Test
    @Timeout(8000)
    public void testIsLineBreak() throws Exception {
        char lf = '\n';
        char cr = '\r';
        char other = 'a';

        assertTrue((boolean) invokePrivateMethod(CSVFormat.class, "isLineBreak", lf));
        assertTrue((boolean) invokePrivateMethod(CSVFormat.class, "isLineBreak", cr));
        assertFalse((boolean) invokePrivateMethod(CSVFormat.class, "isLineBreak", other));
    }

    private Object invokePrivateMethod(Class<?> clazz, String methodName, Object... args) throws Exception {
        try {
            Method method = clazz.getDeclaredMethod(methodName, char.class);
            method.setAccessible(true);
            return method.invoke(null, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Error invoking private method", e);
        }
    }
}