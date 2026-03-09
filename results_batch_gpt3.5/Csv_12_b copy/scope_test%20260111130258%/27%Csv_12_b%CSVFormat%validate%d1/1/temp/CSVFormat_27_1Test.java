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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_27_1Test {

    @Test
    @Timeout(8000)
    public void testValidate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat csvFormat = constructor.newInstance(',', '\"', QuoteMode.ALL, '#', '\\', true, false, "\r\n", null, new String[]{"Header1", "Header2"}, true, true);

        assertThrows(IllegalArgumentException.class, () -> {
            try {
                Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
                validateMethod.setAccessible(true);
                validateMethod.invoke(csvFormat);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        csvFormat = constructor.newInstance(',', '\"', QuoteMode.ALL, null, '\\', true, false, "\r\n", null, new String[]{"Header1", "Header2"}, true, true);

        assertThrows(IllegalArgumentException.class, () -> {
            try {
                Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
                validateMethod.setAccessible(true);
                validateMethod.invoke(csvFormat);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        csvFormat = constructor.newInstance(',', '\"', QuoteMode.ALL, '\"', '\\', true, false, "\r\n", null, new String[]{"Header1", "Header2"}, true, true);

        assertThrows(IllegalArgumentException.class, () -> {
            try {
                Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
                validateMethod.setAccessible(true);
                validateMethod.invoke(csvFormat);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        csvFormat = constructor.newInstance(',', '\"', QuoteMode.ALL, '#', '#', true, false, "\r\n", null, new String[]{"Header1", "Header2"}, true, true);

        assertThrows(IllegalArgumentException.class, () -> {
            try {
                Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
                validateMethod.setAccessible(true);
                validateMethod.invoke(csvFormat);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        csvFormat = constructor.newInstance(',', '\"', QuoteMode.ALL, '#', '#', true, false, "\r\n", null, new String[]{"Header1", "Header2"}, false, true);

        assertThrows(IllegalArgumentException.class, () -> {
            try {
                Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
                validateMethod.setAccessible(true);
                validateMethod.invoke(csvFormat);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        csvFormat = constructor.newInstance(',', '\"', QuoteMode.ALL, '#', '#', null, false, "\r\n", null, new String[]{"Header1", "Header2"}, false, true);

        assertThrows(IllegalArgumentException.class, () -> {
            try {
                Method validateMethod = CSVFormat.class.getDeclaredMethod("validate");
                validateMethod.setAccessible(true);
                validateMethod.invoke(csvFormat);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}