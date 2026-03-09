package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_3_4Test {

    private CSVRecord csvRecord;
    private String[] values;
    private Map<String, Integer> mapping;

    enum TestEnum { col0, col1, col2, col3 }

    @BeforeEach
    void setUp() {
        values = new String[] { "value0", "value1", "value2" };
        mapping = new HashMap<>();
        mapping.put("col0", 0);
        mapping.put("col1", 1);
        mapping.put("col2", 2);
        csvRecord = new CSVRecord(values, mapping, "comment", 123L);
    }

    @Test
    @Timeout(8000)
    void testGetByIndex_validIndices() {
        assertEquals("value0", csvRecord.get(0));
        assertEquals("value1", csvRecord.get(1));
        assertEquals("value2", csvRecord.get(2));
    }

    @Test
    @Timeout(8000)
    void testGetByIndex_indexOutOfBounds_negative() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> csvRecord.get(-1));
    }

    @Test
    @Timeout(8000)
    void testGetByIndex_indexOutOfBounds_tooLarge() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> csvRecord.get(values.length));
    }

    @Test
    @Timeout(8000)
    void testGetPrivateMethod_valuesArray_usingReflection() throws Exception {
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] returnedValues = (String[]) valuesMethod.invoke(csvRecord);
        assertArrayEquals(values, returnedValues);
    }

    @Test
    @Timeout(8000)
    void testIterator() {
        Iterator<String> iter = csvRecord.iterator();
        assertNotNull(iter);
        assertTrue(iter.hasNext());
        assertEquals("value0", iter.next());
        assertEquals("value1", iter.next());
        assertEquals("value2", iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    @Timeout(8000)
    void testSize() {
        assertEquals(values.length, csvRecord.size());
    }

    @Test
    @Timeout(8000)
    void testGetComment() {
        assertEquals("comment", csvRecord.getComment());
    }

    @Test
    @Timeout(8000)
    void testGetRecordNumber() {
        assertEquals(123L, csvRecord.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testIsConsistent() {
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    @Timeout(8000)
    void testIsMapped() {
        assertTrue(csvRecord.isMapped("col0"));
        assertFalse(csvRecord.isMapped("nonexistent"));
    }

    @Test
    @Timeout(8000)
    void testIsSet() {
        assertTrue(csvRecord.isSet("col0"));
        assertFalse(csvRecord.isSet("nonexistent"));
    }

    @Test
    @Timeout(8000)
    void testGetByName() {
        assertEquals("value1", csvRecord.get("col1"));
        assertNull(csvRecord.get("nonexistent"));
    }

    @Test
    @Timeout(8000)
    void testGetByEnum() {
        assertEquals("value0", csvRecord.get(TestEnum.col0));
        assertEquals("value1", csvRecord.get(TestEnum.col1));
        assertEquals("value2", csvRecord.get(TestEnum.col2));
        assertNull(csvRecord.get(TestEnum.col3));
    }

    @Test
    @Timeout(8000)
    void testToListUsingReflection() throws Exception {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.List<String> list = (java.util.List<String>) toListMethod.invoke(csvRecord);
        assertEquals(values.length, list.size());
        assertEquals("value0", list.get(0));
        assertEquals("value1", list.get(1));
        assertEquals("value2", list.get(2));
    }

    @Test
    @Timeout(8000)
    void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals(values.length, map.size());
        assertEquals("value0", map.get("col0"));
        assertEquals("value1", map.get("col1"));
        assertEquals("value2", map.get("col2"));
    }

    @Test
    @Timeout(8000)
    void testToStringContainsValues() {
        String str = csvRecord.toString();
        for (String v : values) {
            assertTrue(str.contains(v));
        }
    }

    @Test
    @Timeout(8000)
    void testPutIn() {
        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> returnedMap = csvRecord.putIn(targetMap);
        assertSame(targetMap, returnedMap);
        assertEquals(values.length, targetMap.size());
        assertEquals("value0", targetMap.get("col0"));
        assertEquals("value1", targetMap.get("col1"));
        assertEquals("value2", targetMap.get("col2"));
    }

}