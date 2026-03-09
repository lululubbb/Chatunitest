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
import java.lang.reflect.Method;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVRecord_1_5Test {

    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    public void setUp() {
        values = new String[]{"val0", "val1", "val2"};
        mapping = new HashMap<>();
        mapping.put("col0", 0);
        mapping.put("col1", 1);
        mapping.put("col2", 2);
        comment = "This is a comment";
        recordNumber = 42L;
    }

    private CSVRecord createCSVRecord(String[] vals, Map<String, Integer> map, String comm, long recNum) throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        return constructor.newInstance(vals, map, comm, recNum);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_NullValues() throws Exception {
        CSVRecord record = createCSVRecord(null, mapping, comment, recordNumber);
        assertNotNull(record);
        assertEquals(recordNumber, record.getRecordNumber());
        assertEquals(comment, record.getComment());
        assertEquals(0, record.size());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_Normal() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertNotNull(record);
        assertEquals(recordNumber, record.getRecordNumber());
        assertEquals(comment, record.getComment());
        assertEquals(values.length, record.size());
    }

    @Test
    @Timeout(8000)
    public void testGetByIndex_Valid() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], record.get(i));
        }
    }

    @Test
    @Timeout(8000)
    public void testGetByIndex_OutOfBounds() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertThrows(IndexOutOfBoundsException.class, () -> record.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> record.get(values.length));
    }

    @Test
    @Timeout(8000)
    public void testGetByName_Valid() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        for (Map.Entry<String, Integer> e : mapping.entrySet()) {
            assertEquals(values[e.getValue()], record.get(e.getKey()));
        }
    }

    @Test
    @Timeout(8000)
    public void testGetByName_Invalid() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertThrows(IllegalArgumentException.class, () -> record.get("nonexistent"));
        assertThrows(NullPointerException.class, () -> record.get((String) null));
    }

    @Test
    @Timeout(8000)
    public void testGetByEnum_Valid() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        Enum<?> e = TestEnum.valueOf("col1");
        assertEquals(values[1], record.get(e));
    }

    @Test
    @Timeout(8000)
    public void testGetByEnum_Null() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertThrows(NullPointerException.class, () -> record.get((Enum<?>) null));
    }

    @Test
    @Timeout(8000)
    public void testGetCommentAndRecordNumber() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_True() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_False() throws Exception {
        // Adjust mapping to only include keys for vals length to ensure inconsistency
        Map<String, Integer> shortMapping = new HashMap<>();
        shortMapping.put("col0", 0);
        shortMapping.put("col1", 1);
        String[] vals = new String[]{"val0", "val1"};
        CSVRecord record = createCSVRecord(vals, shortMapping, comment, recordNumber);
        assertFalse(record.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsMapped() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isMapped("col0"));
        assertFalse(record.isMapped("nonexistent"));
        assertThrows(NullPointerException.class, () -> record.isMapped(null));
    }

    @Test
    @Timeout(8000)
    public void testIsSet() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isSet("col0"));
        assertFalse(record.isSet("nonexistent"));
        assertThrows(NullPointerException.class, () -> record.isSet(null));
    }

    @Test
    @Timeout(8000)
    public void testIterator() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        Iterator<String> it = record.iterator();
        int count = 0;
        while (it.hasNext()) {
            assertEquals(values[count], it.next());
            count++;
        }
        assertEquals(values.length, count);
    }

    @Test
    @Timeout(8000)
    public void testPutIn() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> target = new HashMap<>();
        Map<String, String> result = record.putIn(target);
        assertSame(target, result);
        for (Map.Entry<String, Integer> e : mapping.entrySet()) {
            assertEquals(values[e.getValue()], target.get(e.getKey()));
        }
    }

    @Test
    @Timeout(8000)
    public void testSize() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        assertEquals(values.length, record.size());
    }

    @Test
    @Timeout(8000)
    public void testToList() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(record);
        assertEquals(values.length, list.size());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], list.get(i));
        }
    }

    @Test
    @Timeout(8000)
    public void testToMap() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> map = record.toMap();
        for (Map.Entry<String, Integer> e : mapping.entrySet()) {
            assertEquals(values[e.getValue()], map.get(e.getKey()));
        }
    }

    @Test
    @Timeout(8000)
    public void testToString() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        String str = record.toString();
        assertNotNull(str);
        for (String val : values) {
            assertTrue(str.contains(val));
        }
    }

    @Test
    @Timeout(8000)
    public void testValues() throws Exception {
        CSVRecord record = createCSVRecord(values, mapping, comment, recordNumber);
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] vals = (String[]) valuesMethod.invoke(record);
        assertArrayEquals(values, vals);
    }

    private enum TestEnum {
        col0, col1, col2
    }
}