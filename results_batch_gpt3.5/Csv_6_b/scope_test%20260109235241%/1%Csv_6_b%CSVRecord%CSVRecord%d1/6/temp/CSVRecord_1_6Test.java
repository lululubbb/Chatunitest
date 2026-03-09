package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_1_6Test {

    private Constructor<CSVRecord> constructor;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
    }

    private CSVRecord createCSVRecord(String[] values, Map<String, Integer> mapping, String comment, long recordNumber) throws Exception {
        return constructor.newInstance(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullValues() throws Exception {
        CSVRecord record = createCSVRecord(null, null, null, 5L);
        // values should be EMPTY_STRING_ARRAY
        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        String[] values = (String[]) valuesField.get(record);
        assertNotNull(values);
        assertEquals(0, values.length);

        // comment is null
        assertNull(record.getComment());

        // recordNumber set correctly
        assertEquals(5L, record.getRecordNumber());

        // mapping is null
        Field mappingField = CSVRecord.class.getDeclaredField("mapping");
        mappingField.setAccessible(true);
        assertNull(mappingField.get(record));
    }

    @Test
    @Timeout(8000)
    void testConstructor_nonNullValuesAndMapping() throws Exception {
        String[] values = new String[] {"a", "b", "c"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("one", 0);
        mapping.put("two", 1);
        CSVRecord record = createCSVRecord(values, mapping, "comment", 10L);
        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        assertArrayEquals(values, (String[]) valuesField.get(record));

        Field mappingField = CSVRecord.class.getDeclaredField("mapping");
        mappingField.setAccessible(true);
        assertEquals(mapping, mappingField.get(record));

        assertEquals("comment", record.getComment());
        assertEquals(10L, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testGetByIndex() throws Exception {
        String[] values = new String[] {"x", "y", "z"};
        CSVRecord record = createCSVRecord(values, null, null, 1L);

        assertEquals("x", record.get(0));
        assertEquals("y", record.get(1));
        assertEquals("z", record.get(2));

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(3));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(-1));
    }

    private enum TestEnum {A, B}

    @Test
    @Timeout(8000)
    void testGetByEnum() throws Exception {
        String[] values = new String[] {"valA", "valB"};
        CSVRecord record = createCSVRecord(values, null, null, 1L);

        assertEquals("valA", record.get(TestEnum.A));
        assertEquals("valB", record.get(TestEnum.B));

        // Enum ordinal out of bounds
        Enum<?> invalidEnum = new Enum<TestEnum>("INVALID", 5) {};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(invalidEnum));
    }

    @Test
    @Timeout(8000)
    void testGetByName() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = createCSVRecord(values, mapping, null, 1L);

        assertEquals("v1", record.get("col1"));
        assertEquals("v2", record.get("col2"));

        assertThrows(IllegalArgumentException.class, () -> record.get("col3"));
        assertThrows(NullPointerException.class, () -> record.get(null));
    }

    @Test
    @Timeout(8000)
    void testGetCommentAndRecordNumber() throws Exception {
        CSVRecord record = createCSVRecord(new String[0], null, "myComment", 123L);
        assertEquals("myComment", record.getComment());
        assertEquals(123L, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testIsConsistent() throws Exception {
        String[] values = new String[] {"a", "b"};
        CSVRecord record = createCSVRecord(values, null, null, 1L);

        // consistent if all rows have same length as values length
        assertTrue(record.isConsistent());

        // Create CSVRecord with mapping and values length mismatch
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        CSVRecord record2 = createCSVRecord(new String[] {"a", "b"}, mapping, null, 1L);
        assertFalse(record2.isConsistent());
    }

    @Test
    @Timeout(8000)
    void testIsMapped() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord record = createCSVRecord(new String[] {"val"}, mapping, null, 1L);

        assertTrue(record.isMapped("col1"));
        assertFalse(record.isMapped("col2"));
        assertFalse(record.isMapped(null));
    }

    @Test
    @Timeout(8000)
    void testIsSet() throws Exception {
        String[] values = new String[] {"val1", null};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = createCSVRecord(values, mapping, null, 1L);

        assertTrue(record.isSet("col1"));
        assertFalse(record.isSet("col2"));
        assertFalse(record.isSet("col3"));
        assertFalse(record.isSet(null));
    }

    @Test
    @Timeout(8000)
    void testIterator() throws Exception {
        String[] values = new String[] {"a", "b", "c"};
        CSVRecord record = createCSVRecord(values, null, null, 1L);

        Iterator<String> it = record.iterator();
        List<String> result = new ArrayList<>();
        while (it.hasNext()) {
            result.add(it.next());
        }
        assertEquals(Arrays.asList(values), result);
    }

    @Test
    @Timeout(8000)
    void testPutIn() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = createCSVRecord(values, mapping, null, 1L);

        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> returnedMap = record.putIn(targetMap);
        assertSame(targetMap, returnedMap);
        assertEquals("v1", targetMap.get("col1"));
        assertEquals("v2", targetMap.get("col2"));

        // Test putIn with null map (should throw NPE)
        assertThrows(NullPointerException.class, () -> record.putIn(null));
    }

    @Test
    @Timeout(8000)
    void testSize() throws Exception {
        String[] values = new String[] {"a", "b", "c"};
        CSVRecord record = createCSVRecord(values, null, null, 1L);
        assertEquals(3, record.size());

        CSVRecord emptyRecord = createCSVRecord(null, null, null, 1L);
        assertEquals(0, emptyRecord.size());
    }

    @Test
    @Timeout(8000)
    void testToList() throws Exception {
        String[] values = new String[] {"x", "y"};
        CSVRecord record = createCSVRecord(values, null, null, 1L);

        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(record);

        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    void testToMap() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = createCSVRecord(values, mapping, null, 1L);

        Map<String, String> map = record.toMap();
        assertEquals(2, map.size());
        assertEquals("v1", map.get("col1"));
        assertEquals("v2", map.get("col2"));

        // When mapping is null, toMap returns empty map
        CSVRecord record2 = createCSVRecord(values, null, null, 1L);
        Map<String, String> map2 = record2.toMap();
        assertTrue(map2.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testToString() throws Exception {
        String[] values = new String[] {"a", "b"};
        CSVRecord record = createCSVRecord(values, null, "comment", 1L);
        String s = record.toString();
        assertNotNull(s);
        assertTrue(s.contains("a"));
        assertTrue(s.contains("b"));
    }

    @Test
    @Timeout(8000)
    void testValuesArrayAccess() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        CSVRecord record = createCSVRecord(values, null, null, 1L);
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] returnedValues = (String[]) valuesMethod.invoke(record);
        assertArrayEquals(values, returnedValues);
    }
}