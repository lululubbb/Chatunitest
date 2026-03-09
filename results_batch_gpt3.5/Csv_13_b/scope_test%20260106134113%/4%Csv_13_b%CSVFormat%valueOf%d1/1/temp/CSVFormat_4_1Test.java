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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.Predefined;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_4_1Test {

    @Test
    @Timeout(8000)
    void testValueOf_withValidFormatName() throws Exception {
        // Backup original enum constants array
        Predefined[] originalValues = Predefined.values();

        // Create a mock Predefined instance
        Predefined predefinedMock = Mockito.mock(Predefined.class);
        CSVFormat expectedFormat = CSVFormat.DEFAULT;
        Mockito.when(predefinedMock.getFormat()).thenReturn(expectedFormat);
        Mockito.when(predefinedMock.name()).thenReturn("DEFAULT");
        Mockito.when(predefinedMock.ordinal()).thenReturn(0);

        // Replace the enum constants array with a new one containing the mock for "DEFAULT"
        Field valuesField = null;
        Field[] fields = Predefined.class.getDeclaredFields();
        for (Field f : fields) {
            if (f.getName().contains("$VALUES")) {
                valuesField = f;
                break;
            }
        }
        if (valuesField == null) {
            throw new NoSuchFieldException("$VALUES field not found in Predefined enum");
        }
        valuesField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(valuesField, valuesField.getModifiers() & ~Modifier.FINAL);

        // Create new enum array with the mock replacing the original DEFAULT
        Predefined[] newValues = new Predefined[originalValues.length];
        for (int i = 0; i < originalValues.length; i++) {
            if ("DEFAULT".equals(originalValues[i].name())) {
                newValues[i] = predefinedMock;
            } else {
                newValues[i] = originalValues[i];
            }
        }

        valuesField.set(null, newValues);

        try {
            CSVFormat actualFormat = CSVFormat.valueOf("DEFAULT");
            assertSame(expectedFormat, actualFormat);
        } finally {
            // Restore original enum constants array
            valuesField.set(null, originalValues);
        }
    }

    @Test
    @Timeout(8000)
    void testValueOf_withNullFormatName_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }

    @Test
    @Timeout(8000)
    void testValueOf_withUnknownFormatName_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("UNKNOWN"));
    }
}