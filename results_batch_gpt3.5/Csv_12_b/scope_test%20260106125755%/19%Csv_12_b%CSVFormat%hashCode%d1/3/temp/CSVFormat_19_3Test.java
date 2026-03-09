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
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.Arrays;

class CSVFormatHashCodeTest {

    @Test
    @Timeout(8000)
    void testHashCode_AllFieldsSet() throws Exception {
        String[] header = new String[] {"a", "b"};
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        ctor.setAccessible(true);

        CSVFormat format = ctor.newInstance(
                ';',
                '\"',
                QuoteMode.ALL,
                '#',
                '\\',
                true,
                false,
                "\r\n",
                "NULL",
                header,
                true,
                false);

        int expected = 1;
        int prime = 31;
        expected = prime * expected + ';';
        expected = prime * expected + QuoteMode.ALL.hashCode();
        expected = prime * expected + Character.valueOf('\"').hashCode();
        expected = prime * expected + Character.valueOf('#').hashCode();
        expected = prime * expected + Character.valueOf('\\').hashCode();
        expected = prime * expected + "NULL".hashCode();
        expected = prime * expected + (true ? 1231 : 1237);
        expected = prime * expected + (false ? 1231 : 1237);
        expected = prime * expected + (true ? 1231 : 1237);
        expected = prime * expected + "\r\n".hashCode();
        expected = prime * expected + Arrays.hashCode(header);

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_NullableFieldsNull() throws Exception {
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        ctor.setAccessible(true);

        CSVFormat format = ctor.newInstance(
                ',',
                null,
                null,
                null,
                null,
                false,
                true,
                null,
                null,
                null,
                false,
                false);

        int expected = 1;
        int prime = 31;
        expected = prime * expected + ',';
        expected = prime * expected + 0;
        expected = prime * expected + 0;
        expected = prime * expected + 0;
        expected = prime * expected + 0;
        expected = prime * expected + 0;
        expected = prime * expected + (false ? 1231 : 1237);
        expected = prime * expected + (true ? 1231 : 1237);
        expected = prime * expected + (false ? 1231 : 1237);
        expected = prime * expected + 0;
        expected = prime * expected + 0;

        assertEquals(expected, format.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_DifferentHeaders() throws Exception {
        String[] header1 = new String[] {"x"};
        String[] header2 = new String[] {"y"};

        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        ctor.setAccessible(true);

        CSVFormat format1 = ctor.newInstance(
                ',',
                '\"',
                QuoteMode.MINIMAL,
                null,
                null,
                false,
                false,
                "\n",
                null,
                header1,
                false,
                false);

        CSVFormat format2 = ctor.newInstance(
                ',',
                '\"',
                QuoteMode.MINIMAL,
                null,
                null,
                false,
                false,
                "\n",
                null,
                header2,
                false,
                false);

        assertNotEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    @Timeout(8000)
    void testHashCode_Consistency() {
        CSVFormat format = CSVFormat.DEFAULT;
        int first = format.hashCode();
        int second = format.hashCode();
        assertEquals(first, second);
    }

    @Test
    @Timeout(8000)
    void testHashCode_PrivateConstructorAndFields() throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;
        Constructor<CSVFormat> ctor = clazz.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class, boolean.class);
        ctor.setAccessible(true);

        String[] header = new String[] {"h1", "h2"};
        CSVFormat format = ctor.newInstance(
                '|',
                'q',
                QuoteMode.NON_NUMERIC,
                '!',
                '*',
                true,
                true,
                "\n",
                "nullStr",
                header,
                false,
                false);

        Method hashCodeMethod = clazz.getDeclaredMethod("hashCode");
        hashCodeMethod.setAccessible(true);
        int hash = (int) hashCodeMethod.invoke(format);

        int expected = 1;
        int prime = 31;
        expected = prime * expected + '|';
        expected = prime * expected + QuoteMode.NON_NUMERIC.hashCode();
        expected = prime * expected + Character.valueOf('q').hashCode();
        expected = prime * expected + Character.valueOf('!').hashCode();
        expected = prime * expected + Character.valueOf('*').hashCode();
        expected = prime * expected + "nullStr".hashCode();
        expected = prime * expected + (true ? 1231 : 1237);
        expected = prime * expected + (true ? 1231 : 1237);
        expected = prime * expected + (false ? 1231 : 1237);
        expected = prime * expected + "\n".hashCode();
        expected = prime * expected + Arrays.hashCode(header);

        assertEquals(expected, hash);
    }
}