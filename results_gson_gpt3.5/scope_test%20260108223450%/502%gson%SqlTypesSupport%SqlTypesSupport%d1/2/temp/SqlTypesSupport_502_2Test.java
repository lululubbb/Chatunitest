package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType;
import com.google.gson.TypeAdapterFactory;
import java.util.Date;

class SqlTypesSupport_502_2Test {

  @Test
    @Timeout(8000)
  void testStaticFieldsInitialization() throws Exception {
    // Access class object
    Class<SqlTypesSupport> clazz = SqlTypesSupport.class;

    // SUPPORTS_SQL_TYPES should be boolean and initialized
    Field supportsSqlTypesField = clazz.getDeclaredField("SUPPORTS_SQL_TYPES");
    supportsSqlTypesField.setAccessible(true);
    assertTrue(Modifier.isStatic(supportsSqlTypesField.getModifiers()));
    assertEquals(boolean.class, supportsSqlTypesField.getType());
    Object supportsSqlTypesValue = supportsSqlTypesField.get(null);
    assertNotNull(supportsSqlTypesValue);
    assertTrue(supportsSqlTypesValue instanceof Boolean);

    // DATE_DATE_TYPE should be DateType<? extends Date> and initialized
    Field dateDateTypeField = clazz.getDeclaredField("DATE_DATE_TYPE");
    dateDateTypeField.setAccessible(true);
    assertTrue(Modifier.isStatic(dateDateTypeField.getModifiers()));
    assertTrue(DateType.class.isAssignableFrom(dateDateTypeField.getType()));
    Object dateDateTypeValue = dateDateTypeField.get(null);
    assertNotNull(dateDateTypeValue);

    // TIMESTAMP_DATE_TYPE should be DateType<? extends Date> and initialized
    Field timestampDateTypeField = clazz.getDeclaredField("TIMESTAMP_DATE_TYPE");
    timestampDateTypeField.setAccessible(true);
    assertTrue(Modifier.isStatic(timestampDateTypeField.getModifiers()));
    assertTrue(DateType.class.isAssignableFrom(timestampDateTypeField.getType()));
    Object timestampDateTypeValue = timestampDateTypeField.get(null);
    assertNotNull(timestampDateTypeValue);

    // DATE_FACTORY should be TypeAdapterFactory and initialized
    Field dateFactoryField = clazz.getDeclaredField("DATE_FACTORY");
    dateFactoryField.setAccessible(true);
    assertTrue(Modifier.isStatic(dateFactoryField.getModifiers()));
    assertTrue(TypeAdapterFactory.class.isAssignableFrom(dateFactoryField.getType()));
    Object dateFactoryValue = dateFactoryField.get(null);
    assertNotNull(dateFactoryValue);

    // TIME_FACTORY should be TypeAdapterFactory and initialized
    Field timeFactoryField = clazz.getDeclaredField("TIME_FACTORY");
    timeFactoryField.setAccessible(true);
    assertTrue(Modifier.isStatic(timeFactoryField.getModifiers()));
    assertTrue(TypeAdapterFactory.class.isAssignableFrom(timeFactoryField.getType()));
    Object timeFactoryValue = timeFactoryField.get(null);
    assertNotNull(timeFactoryValue);

    // TIMESTAMP_FACTORY should be TypeAdapterFactory and initialized
    Field timestampFactoryField = clazz.getDeclaredField("TIMESTAMP_FACTORY");
    timestampFactoryField.setAccessible(true);
    assertTrue(Modifier.isStatic(timestampFactoryField.getModifiers()));
    assertTrue(TypeAdapterFactory.class.isAssignableFrom(timestampFactoryField.getType()));
    Object timestampFactoryValue = timestampFactoryField.get(null);
    assertNotNull(timestampFactoryValue);
  }
}