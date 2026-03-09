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
import java.lang.reflect.Method;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_1_4Test {

    private Constructor<CSVRecord> constructor;

    @BeforeEach
    public void setup() throws Exception {
        constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_NullValues() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord record = constructor.newInstance(null, mapping, "comment", 123L);

        // values should be EMPTY_STRING_ARRAY (length 0)
        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        String[] values = (String[]) valuesField.get(record);
        assertNotNull(values);
        assertEquals(0, values.length);

        // mapping should be set correctly
        Field mappingField = CSVRecord.class.getDeclaredField("mapping");
        mappingField.setAccessible(true);
        Map<String, Integer> map = (Map<String, Integer>) mappingField.get(record);
        assertEquals(mapping, map);

        // comment and recordNumber
        assertEquals("comment", record.getComment());
        assertEquals(123L, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_WithValues() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord record = constructor.newInstance(values, mapping, null, 0L);

        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        String[] storedValues = (String[]) valuesField.get(record);
        assertArrayEquals(values, storedValues);

        assertNull(record.getComment());
        assertEquals(0L, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testGetByIndex_Valid() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        CSVRecord record = constructor.newInstance(values, null, null, 1L);

        assertEquals("v1", record.get(0));
        assertEquals("v2", record.get(1));
    }

    @Test
    @Timeout(8000)
    public void testGetByIndex_OutOfBounds() throws Exception {
        String[] values = new String[] {"v1"};
        CSVRecord record = constructor.newInstance(values, null, null, 1L);

        assertThrows(IndexOutOfBoundsException.class, () -> record.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> record.get(-1));
    }

    @Test
    @Timeout(8000)
    public void testGetByName_Valid() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = constructor.newInstance(values, mapping, null, 1L);

        assertEquals("v1", record.get("col1"));
        assertEquals("v2", record.get("col2"));
    }

    @Test
    @Timeout(8000)
    public void testGetByName_InvalidName() throws Exception {
        String[] values = new String[] {"v1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord record = constructor.newInstance(values, mapping, null, 1L);

        assertThrows(IllegalArgumentException.class, () -> record.get("unknown"));
        assertThrows(NullPointerException.class, () -> record.get((String) null));
    }

    @Test
    @Timeout(8000)
    public void testGetByEnum() throws Exception {
        String[] values = new String[] {"v1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("ENUM", 0);
        CSVRecord record = constructor.newInstance(values, mapping, null, 1L);

        Enum<?> e = mock(Enum.class);
        when(e.name()).thenReturn("ENUM");
        assertEquals("v1", record.get(e));
    }

    @Test
    @Timeout(8000)
    public void testGetByEnum_NullEnum() throws Exception {
        String[] values = new String[] {"v1"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("ENUM", 0);
        CSVRecord record = constructor.newInstance(values, mapping, null, 1L);

        assertThrows(NullPointerException.class, () -> record.get((Enum<?>) null));
    }

    @Test
    @Timeout(8000)
    public void testGetComment() throws Exception {
        CSVRecord record = constructor.newInstance(null, null, "myComment", 1L);
        assertEquals("myComment", record.getComment());

        CSVRecord recordNull = constructor.newInstance(null, null, null, 1L);
        assertNull(recordNull.getComment());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber() throws Exception {
        CSVRecord record = constructor.newInstance(null, null, null, 999L);
        assertEquals(999L, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_True() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        CSVRecord record = constructor.newInstance(values, null, null, 1L);
        assertTrue(record.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_False() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 2); // index 2 out of bounds

        CSVRecord record = constructor.newInstance(values, mapping, null, 1L);
        assertFalse(record.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsMapped() throws Exception {
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        CSVRecord record = constructor.newInstance(null, mapping, null, 1L);

        assertTrue(record.isMapped("col1"));
        assertFalse(record.isMapped("col2"));
        assertFalse(record.isMapped(null));
    }

    @Test
    @Timeout(8000)
    public void testIsSet() throws Exception {
        String[] values = new String[] {"v1", null};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = constructor.newInstance(values, mapping, null, 1L);

        assertTrue(record.isSet("col1"));
        assertFalse(record.isSet("col2"));
        assertFalse(record.isSet("col3"));
        assertFalse(record.isSet(null));
    }

    @Test
    @Timeout(8000)
    public void testIterator() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        CSVRecord record = constructor.newInstance(values, null, null, 1L);

        Iterator<String> it = record.iterator();
        assertTrue(it.hasNext());
        assertEquals("v1", it.next());
        assertTrue(it.hasNext());
        assertEquals("v2", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    @Timeout(8000)
    public void testPutIn() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = constructor.newInstance(values, mapping, null, 1L);

        Map<String, String> targetMap = new HashMap<>();
        Map<String, String> result = record.putIn(targetMap);

        assertSame(targetMap, result);
        assertEquals("v1", targetMap.get("col1"));
        assertEquals("v2", targetMap.get("col2"));
    }

    @Test
    @Timeout(8000)
    public void testSize() throws Exception {
        String[] values = new String[] {"v1", "v2", "v3"};
        CSVRecord record = constructor.newInstance(values, null, null, 1L);
        assertEquals(3, record.size());
    }

    @Test
    @Timeout(8000)
    public void testToList_PrivateMethod() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        CSVRecord record = constructor.newInstance(values, null, null, 1L);

        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(record);

        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    public void testToMap() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        CSVRecord record = constructor.newInstance(values, mapping, null, 1L);

        Map<String, String> map = record.toMap();
        assertEquals(2, map.size());
        assertEquals("v1", map.get("col1"));
        assertEquals("v2", map.get("col2"));
    }

    @Test
    @Timeout(8000)
    public void testToString() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        CSVRecord record = constructor.newInstance(values, null, null, 1L);

        String str = record.toString();
        assertNotNull(str);
        assertTrue(str.contains("v1"));
        assertTrue(str.contains("v2"));
    }

    @Test
    @Timeout(8000)
    public void testValues_PrivateMethod() throws Exception {
        String[] values = new String[] {"v1", "v2"};
        CSVRecord record = constructor.newInstance(values, null, null, 1L);

        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] returnedValues = (String[]) valuesMethod.invoke(record);

        assertArrayEquals(values, returnedValues);
    }
}