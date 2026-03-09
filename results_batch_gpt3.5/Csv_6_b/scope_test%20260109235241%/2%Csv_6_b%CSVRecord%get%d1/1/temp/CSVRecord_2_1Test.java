package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class CSVRecord_2_1Test {

    @Test
    @Timeout(8000)
    void testGet_withEnum_returnsValueForEnumName() throws Exception {
        // Arrange
        String[] values = new String[] { "value0", "value1", "value2" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("ENUM_VALUE", 1);
        String comment = "comment";
        long recordNumber = 123L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        // Create a mock enum with name() returning "ENUM_VALUE"
        Enum<?> mockEnum = mock(Enum.class);
        when(mockEnum.toString()).thenReturn("ENUM_VALUE");

        // Act
        // Use reflection to call CSVRecord.get(Enum)
        Method getEnumMethod = CSVRecord.class.getMethod("get", Enum.class);
        String result = (String) getEnumMethod.invoke(record, mockEnum);

        // Assert
        assertEquals("value1", result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_returnsNullIfMappingNotFound() throws Exception {
        // Arrange
        String[] values = new String[] { "value0", "value1" };
        Map<String, Integer> mapping = new HashMap<>();
        // mapping is empty, no entries
        String comment = null;
        long recordNumber = 0L;

        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);

        Enum<?> mockEnum = mock(Enum.class);
        when(mockEnum.toString()).thenReturn("NON_EXISTENT_KEY");

        // Act
        Method getEnumMethod = CSVRecord.class.getMethod("get", Enum.class);
        String result = (String) getEnumMethod.invoke(record, mockEnum);

        // Assert
        // Since mapping does not contain the key, get(Enum) will return null
        assertEquals(null, result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_invokesGetStringMethod() throws Exception {
        // Arrange
        String[] values = new String[] { "v0", "v1" };
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("ENUM_NAME", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 0L);

        Enum<?> mockEnum = mock(Enum.class);
        when(mockEnum.toString()).thenReturn("ENUM_NAME");

        // Use reflection to get private get(String) method and public get(Enum) method
        Method getStringMethod = CSVRecord.class.getDeclaredMethod("get", String.class);
        getStringMethod.setAccessible(true);
        Method getEnumMethod = CSVRecord.class.getMethod("get", Enum.class);

        // Act
        String expected = (String) getStringMethod.invoke(record, "ENUM_NAME");
        String actual = (String) getEnumMethod.invoke(record, mockEnum);

        // Assert
        assertEquals(expected, actual);
    }
}