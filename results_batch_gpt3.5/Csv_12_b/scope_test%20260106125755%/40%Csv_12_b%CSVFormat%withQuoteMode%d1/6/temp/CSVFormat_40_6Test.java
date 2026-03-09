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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class CSVFormat_40_6Test {

    @Test
    @Timeout(8000)
    void testWithQuoteModeReturnsNewInstanceWithCorrectQuoteMode() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        QuoteMode newQuoteMode = QuoteMode.ALL;

        CSVFormat modified = original.withQuoteMode(newQuoteMode);

        assertNotSame(original, modified, "withQuoteMode should return a new CSVFormat instance");
        assertEquals(newQuoteMode, modified.getQuoteMode(), "QuoteMode should be set to the new value");

        // All other fields should remain unchanged - use reflection to compare private fields except quoteMode
        Field[] fields = CSVFormat.class.getDeclaredFields();
        for (Field field : fields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                continue; // skip static fields
            }
            if ("quoteMode".equals(field.getName())) {
                continue; // skip quoteMode field
            }
            field.setAccessible(true);
            Object originalValue = field.get(original);
            Object modifiedValue = field.get(modified);
            if (originalValue != null && originalValue.getClass().isArray()) {
                assertArrayEquals((Object[]) originalValue, (Object[]) modifiedValue,
                        "Field '" + field.getName() + "' should be unchanged");
            } else {
                assertEquals(originalValue, modifiedValue, "Field '" + field.getName() + "' should be unchanged");
            }
        }

        // Test with null QuoteMode
        CSVFormat nullQuoteModeFormat = original.withQuoteMode(null);
        assertNull(nullQuoteModeFormat.getQuoteMode(), "QuoteMode should be null when set to null");
    }
}