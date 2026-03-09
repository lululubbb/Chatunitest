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

class CSVFormat_4_2Test {

    @Test
    @Timeout(8000)
    void testValueOf_withValidPredefinedFormat() throws Exception {
        // Arrange
        String formatName = "DEFAULT";
        CSVFormat expectedFormat = CSVFormat.DEFAULT;

        // Use reflection to replace the enum constant with a mock
        Class<Predefined> predefinedClass = Predefined.class;

        // Get the enum constant field
        Field enumField = predefinedClass.getDeclaredField(formatName);
        enumField.setAccessible(true);

        // Remove final modifier from the field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(enumField, enumField.getModifiers() & ~Modifier.FINAL);

        // Create mock Predefined
        Predefined predefinedMock = Mockito.mock(Predefined.class);
        Mockito.when(predefinedMock.getFormat()).thenReturn(expectedFormat);

        // Replace enum constant with mock
        enumField.set(null, predefinedMock);

        // Act
        CSVFormat actualFormat = CSVFormat.valueOf(formatName);

        // Assert
        assertSame(expectedFormat, actualFormat);
        Mockito.verify(predefinedMock).getFormat();
    }

    @Test
    @Timeout(8000)
    void testValueOf_withInvalidFormat_throwsIllegalArgumentException() {
        String invalidFormatName = "NON_EXISTENT_FORMAT";

        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(invalidFormatName));
    }

}