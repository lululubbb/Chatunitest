package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class CSVRecord_2_3Test {

    private enum TestEnum {
        ENUM_NAME,
        NON_EXISTENT
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_existingMapping() throws Exception {
        // Arrange
        String[] values = {"value0", "value1", "value2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("ENUM_NAME", 1);
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        // Use real enum instance
        Enum<?> enumInstance = TestEnum.ENUM_NAME;

        // Act
        String result = record.get(enumInstance);

        // Assert
        assertEquals("value1", result);
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_nonExistingMapping() {
        // Arrange
        String[] values = {"value0", "value1"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        Enum<?> enumInstance = TestEnum.NON_EXISTENT;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> record.get(enumInstance));
    }

    @Test
    @Timeout(8000)
    void testGet_withEnum_nullEnum() {
        // Arrange
        String[] values = {"value0", "value1"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1L);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> record.get((Enum<?>) null));
    }
}