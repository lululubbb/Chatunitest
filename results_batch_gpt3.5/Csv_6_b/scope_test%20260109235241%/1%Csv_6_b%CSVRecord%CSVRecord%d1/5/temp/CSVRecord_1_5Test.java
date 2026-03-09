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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_1_5Test {

    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    private CSVRecord record;

    @BeforeEach
    void setUp() {
        values = new String[]{"val0", "val1", "val2"};
        mapping = new HashMap<>();
        mapping.put("col0", 0);
        mapping.put("col1", 1);
        mapping.put("col2", 2);
        comment = "a comment";
        recordNumber = 42L;

        record = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testConstructorNullValues() throws Exception {
        CSVRecord r = new CSVRecord(null, mapping, comment, recordNumber);

        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        String[] vals = (String[]) valuesField.get(r);
        assertNotNull(vals);
        assertEquals(0, vals.length);
    }

    @Test
    @Timeout(8000)
    void testGetByIndex() {
        assertEquals("val0", record.get(0));
        assertEquals("val1", record.get(1));
        assertEquals("val2", record.get(2));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(3));
    }

    @Test
    @Timeout(8000)
    void testGetByName() {
        assertEquals("val0", record.get("col0"));
        assertEquals("val1", record.get("col1"));
        assertEquals("val2", record.get("col2"));
        assertThrows(IllegalArgumentException.class, () -> record.get("unknown"));
        assertThrows(IllegalArgumentException.class, () -> record.get((String) null));
    }

    private enum TestEnum {
        col0, col1
    }

    @Test
    @Timeout(8000)
    void testGetByEnum() {
        assertEquals("val0", record.get(TestEnum.col0));
        assertEquals("val1", record.get(TestEnum.col1));
        assertThrows(IllegalArgumentException.class, () -> record.get((Enum<?>) null));
    }

    @Test
    @Timeout(8000)
    void testGetComment() {
        assertEquals(comment, record.getComment());
        CSVRecord r = new CSVRecord(values, mapping, null, recordNumber);
        assertNull(r.getComment());
    }

    @Test
    @Timeout(8000)
    void testGetRecordNumber() {
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testIsConsistent() {
        assertTrue(record.isConsistent());

        Map<String, Integer> map2 = new HashMap<>();
        map2.put("a", 0);
        map2.put("b", 1);
        CSVRecord inconsistent = new CSVRecord(new String[] {"v0"}, map2, null, 1);
        assertFalse(inconsistent.isConsistent());
    }

    @Test
    @Timeout(8000)
    void testIsMapped() {
        assertTrue(record.isMapped("col0"));
        assertFalse(record.isMapped("unknown"));
        assertFalse(record.isMapped(null));
    }

    @Test
    @Timeout(8000)
    void testIsSet() {
        assertTrue(record.isSet("col0"));
        assertFalse(record.isSet("unknown"));
        assertFalse(record.isSet(null));

        CSVRecord r = new CSVRecord(new String[] {null}, mapping, null, 1);
        assertFalse(r.isSet("col0"));
    }

    @Test
    @Timeout(8000)
    void testIterator() {
        Iterator<String> it = record.iterator();
        List<String> list = new ArrayList<>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    void testPutIn() {
        Map<String, String> target = new HashMap<>();
        Map<String, String> returned = record.putIn(target);
        assertSame(target, returned);
        assertEquals(3, target.size());
        assertEquals("val0", target.get("col0"));
        assertEquals("val1", target.get("col1"));
        assertEquals("val2", target.get("col2"));
    }

    @Test
    @Timeout(8000)
    void testSize() {
        assertEquals(values.length, record.size());
        CSVRecord empty = new CSVRecord(new String[0], mapping, null, 1);
        assertEquals(0, empty.size());
    }

    @Test
    @Timeout(8000)
    void testToList() throws Exception {
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(record);
        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    void testToMap() {
        Map<String, String> map = record.toMap();
        assertEquals(mapping.size(), map.size());
        for (Map.Entry<String, Integer> e : mapping.entrySet()) {
            assertEquals(values[e.getValue().intValue()], map.get(e.getKey()));
        }
    }

    @Test
    @Timeout(8000)
    void testToString() {
        String s = record.toString();
        assertTrue(s.contains("val0"));
        assertTrue(s.contains("val1"));
        assertTrue(s.contains("val2"));
    }

    @Test
    @Timeout(8000)
    void testValuesMethod() throws Exception {
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] vals = (String[]) valuesMethod.invoke(record);
        assertArrayEquals(values, vals);
    }
}