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

public class CSVRecord_1_1Test {

    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;
    private CSVRecord csvRecord;

    @BeforeEach
    public void setUp() throws Exception {
        values = new String[]{"val1", "val2", "val3"};
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        comment = "This is a comment";
        recordNumber = 123L;

        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        csvRecord = constructor.newInstance(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_nullValues_setsEmptyArray() throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord record = constructor.newInstance((Object) null, mapping, comment, recordNumber);
        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        String[] internalValues = (String[]) valuesField.get(record);
        assertNotNull(internalValues);
        assertEquals(0, internalValues.length);
    }

    @Test
    @Timeout(8000)
    public void testGetByIndex_validIndex() {
        assertEquals("val1", csvRecord.get(0));
        assertEquals("val2", csvRecord.get(1));
        assertEquals("val3", csvRecord.get(2));
    }

    @Test
    @Timeout(8000)
    public void testGetByIndex_invalidIndex() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> csvRecord.get(3));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> csvRecord.get(-1));
    }

    @Test
    @Timeout(8000)
    public void testGetByName_existingName() {
        assertEquals("val1", csvRecord.get("col1"));
        assertEquals("val2", csvRecord.get("col2"));
        assertEquals("val3", csvRecord.get("col3"));
    }

    @Test
    @Timeout(8000)
    public void testGetByName_nonExistingName() {
        assertThrows(IllegalArgumentException.class, () -> csvRecord.get("unknown"));
        assertThrows(NullPointerException.class, () -> csvRecord.get((String) null));
    }

    @Test
    @Timeout(8000)
    public void testGetByEnum_existingEnum() {
        Enum<?> e = mock(Enum.class);
        when(e.name()).thenReturn("col2");
        assertEquals("val2", csvRecord.get(e));
    }

    @Test
    @Timeout(8000)
    public void testGetByEnum_nullEnum() {
        assertThrows(NullPointerException.class, () -> csvRecord.get((Enum<?>) null));
    }

    @Test
    @Timeout(8000)
    public void testGetComment() {
        assertEquals(comment, csvRecord.getComment());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber() {
        assertEquals(recordNumber, csvRecord.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_true() {
        // values length == mapping size
        assertTrue(csvRecord.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsConsistent_false() throws Exception {
        // Create CSVRecord with values length != mapping size
        String[] vals = new String[] {"val1", "val2"};
        Map<String, Integer> map = new HashMap<>();
        map.put("col1", 0);
        map.put("col2", 1);
        map.put("col3", 2);
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord record = constructor.newInstance(vals, map, null, 1L);
        assertFalse(record.isConsistent());
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_true() {
        assertTrue(csvRecord.isMapped("col1"));
    }

    @Test
    @Timeout(8000)
    public void testIsMapped_false() {
        assertFalse(csvRecord.isMapped("unknown"));
        assertFalse(csvRecord.isMapped(null));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_true() {
        // value for col1 is "val1" which is not null
        assertTrue(csvRecord.isSet("col1"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_false_nullValue() throws Exception {
        // Create CSVRecord with a null value for a mapped column
        String[] vals = new String[] {null, "val2", "val3"};
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord record = constructor.newInstance(vals, mapping, null, 1L);
        assertFalse(record.isSet("col1"));
    }

    @Test
    @Timeout(8000)
    public void testIsSet_false_unmappedName() {
        assertFalse(csvRecord.isSet("unknown"));
        assertFalse(csvRecord.isSet(null));
    }

    @Test
    @Timeout(8000)
    public void testIterator() {
        Iterator<String> it = csvRecord.iterator();
        List<String> list = new ArrayList<>();
        it.forEachRemaining(list::add);
        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    public void testPutIn() {
        Map<String, String> target = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(target);
        assertSame(target, result);
        assertEquals("val1", target.get("col1"));
        assertEquals("val2", target.get("col2"));
        assertEquals("val3", target.get("col3"));
    }

    @Test
    @Timeout(8000)
    public void testSize() {
        assertEquals(values.length, csvRecord.size());
    }

    @Test
    @Timeout(8000)
    public void testToList() throws Exception {
        Method toList = CSVRecord.class.getDeclaredMethod("toList");
        toList.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toList.invoke(csvRecord);
        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    public void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals(values.length, map.size());
        assertEquals("val1", map.get("col1"));
        assertEquals("val2", map.get("col2"));
        assertEquals("val3", map.get("col3"));
    }

    @Test
    @Timeout(8000)
    public void testToString() {
        String str = csvRecord.toString();
        for (String val : values) {
            assertTrue(str.contains(val));
        }
        if (comment != null) {
            assertTrue(str.contains(comment));
        }
        assertTrue(str.contains(Long.toString(recordNumber)));
    }

    @Test
    @Timeout(8000)
    public void testValuesField() throws Exception {
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] returnedValues = (String[]) valuesMethod.invoke(csvRecord);
        assertArrayEquals(values, returnedValues);
    }
}