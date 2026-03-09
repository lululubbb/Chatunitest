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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class CSVFormat_8_3Test {

    @Test
    @Timeout(8000)
    void testGetDelimiter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_RFC4180() {
        CSVFormat format = CSVFormat.RFC4180;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_EXCEL() {
        CSVFormat format = CSVFormat.EXCEL;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_TDF() {
        CSVFormat format = CSVFormat.TDF;
        assertEquals('\t', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_MYSQL() {
        CSVFormat format = CSVFormat.MYSQL;
        assertEquals('\t', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_NewFormat() {
        CSVFormat format = CSVFormat.newFormat(';');
        assertEquals(';', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_WithDelimiter() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter('|');
        assertEquals('|', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Reflection_PrivateField() throws Exception {
        // Get the nested Quote enum class from CSVFormat
        Class<?> quoteClass = null;
        for (Class<?> innerClass : CSVFormat.class.getDeclaredClasses()) {
            if ("Quote".equals(innerClass.getSimpleName())) {
                quoteClass = innerClass;
                break;
            }
        }

        Constructor<?> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class,
                Character.class,
                quoteClass,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                String[].class,
                boolean.class
        );
        constructor.setAccessible(true);

        // For the 'header' parameter (String[]), pass an empty array instead of null to avoid NPE
        String[] header = new String[0];

        // For the quotePolicy parameter, pass null or the enum constant if needed
        Object quotePolicy = null;

        CSVFormat instance = (CSVFormat) constructor.newInstance(
                ';',
                null,
                quotePolicy,
                null,
                null,
                false,
                false,
                null,
                null,
                header,
                false
        );

        Method method = CSVFormat.class.getDeclaredMethod("getDelimiter");
        method.setAccessible(true);
        char delimiter = (char) method.invoke(instance);

        assertEquals(';', delimiter);
    }
}