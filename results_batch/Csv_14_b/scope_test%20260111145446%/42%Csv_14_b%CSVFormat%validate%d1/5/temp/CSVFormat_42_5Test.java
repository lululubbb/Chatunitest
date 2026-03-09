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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

public class CSVFormat_42_5Test {

    @Test
    @Timeout(8000)
    public void testValidate() {
        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                    Character.class, Character.class, boolean.class, boolean.class, String.class,
                    String.class, Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
            constructor.setAccessible(true);

            CSVFormat csvFormat = constructor.newInstance(',', '"', null, null, null, false, true, "\r\n",
                    null, null, null, false, false, false, false, false);

            assertThrows(IllegalArgumentException.class, () -> {
                try {
                    csvFormat.getClass().getDeclaredMethod("validate").setAccessible(true);
                    csvFormat.validate();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            csvFormat = constructor.newInstance(',', ',', null, null, null, false, true, "\r\n",
                    null, null, null, false, false, false, false, false);

            assertThrows(IllegalArgumentException.class, () -> {
                try {
                    csvFormat.getClass().getDeclaredMethod("validate").setAccessible(true);
                    csvFormat.validate();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            csvFormat = constructor.newInstance(',', '"', '"', null, null, false, true, "\r\n",
                    null, null, null, false, false, false, false, false);

            assertThrows(IllegalArgumentException.class, () -> {
                try {
                    csvFormat.getClass().getDeclaredMethod("validate").setAccessible(true);
                    csvFormat.validate();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            csvFormat = constructor.newInstance(',', '"', null, '"', null, false, true, "\r\n",
                    null, null, null, false, false, false, false, false);

            assertThrows(IllegalArgumentException.class, () -> {
                try {
                    csvFormat.getClass().getDeclaredMethod("validate").setAccessible(true);
                    csvFormat.validate();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            csvFormat = constructor.newInstance(',', '"', null, null, '"', false, true, "\r\n",
                    null, null, null, false, false, false, false, false);

            assertThrows(IllegalArgumentException.class, () -> {
                try {
                    csvFormat.getClass().getDeclaredMethod("validate").setAccessible(true);
                    csvFormat.validate();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            csvFormat = constructor.newInstance(',', '"', null, null, null, false, true, "\r\n",
                    null, null, null, false, false, false, false, false);
            csvFormat.withQuoteMode(QuoteMode.NONE);

            assertThrows(IllegalArgumentException.class, () -> {
                try {
                    csvFormat.getClass().getDeclaredMethod("validate").setAccessible(true);
                    csvFormat.validate();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            csvFormat = constructor.newInstance(',', '"', null, null, null, false, true, "\r\n",
                    null, null, null, false, false, false, false, false);
            csvFormat.withHeader("A", "B", "A");

            assertThrows(IllegalArgumentException.class, () -> {
                try {
                    csvFormat.getClass().getDeclaredMethod("validate").setAccessible(true);
                    csvFormat.validate();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}