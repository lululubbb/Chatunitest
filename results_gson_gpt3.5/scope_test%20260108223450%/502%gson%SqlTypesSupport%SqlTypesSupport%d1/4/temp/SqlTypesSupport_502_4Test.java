package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType;
import com.google.gson.TypeAdapterFactory;

class SqlTypesSupport_502_4Test {

  @Test
    @Timeout(8000)
  void testStaticFieldsInitialization() throws Exception {
    // Access the class object
    Class<SqlTypesSupport> clazz = SqlTypesSupport.class;

    // SUPPORTS_SQL_TYPES should be a boolean and static final
    Field supportsSqlTypesField = clazz.getField("SUPPORTS_SQL_TYPES");
    assertTrue(Modifier.isStatic(supportsSqlTypesField.getModifiers()));
    assertTrue(Modifier.isFinal(supportsSqlTypesField.getModifiers()));
    assertEquals(boolean.class, supportsSqlTypesField.getType());
    boolean supportsSqlTypesValue = supportsSqlTypesField.getBoolean(null);
    // We cannot predict exact value but it should be boolean
    assertNotNull(supportsSqlTypesValue);

    // DATE_DATE_TYPE should be static final and instance of DateType<? extends Date>
    Field dateDateTypeField = clazz.getField("DATE_DATE_TYPE");
    assertTrue(Modifier.isStatic(dateDateTypeField.getModifiers()));
    assertTrue(Modifier.isFinal(dateDateTypeField.getModifiers()));
    Object dateDateTypeValue = dateDateTypeField.get(null);
    assertNotNull(dateDateTypeValue);
    assertTrue(dateDateTypeValue instanceof DateType);
    // The type parameter is erased, so we just check instance of DateType

    // TIMESTAMP_DATE_TYPE should be static final and instance of DateType<? extends Date>
    Field timestampDateTypeField = clazz.getField("TIMESTAMP_DATE_TYPE");
    assertTrue(Modifier.isStatic(timestampDateTypeField.getModifiers()));
    assertTrue(Modifier.isFinal(timestampDateTypeField.getModifiers()));
    Object timestampDateTypeValue = timestampDateTypeField.get(null);
    assertNotNull(timestampDateTypeValue);
    assertTrue(timestampDateTypeValue instanceof DateType);

    // DATE_FACTORY should be static final and instance of TypeAdapterFactory
    Field dateFactoryField = clazz.getField("DATE_FACTORY");
    assertTrue(Modifier.isStatic(dateFactoryField.getModifiers()));
    assertTrue(Modifier.isFinal(dateFactoryField.getModifiers()));
    Object dateFactoryValue = dateFactoryField.get(null);
    assertNotNull(dateFactoryValue);
    assertTrue(dateFactoryValue instanceof TypeAdapterFactory);

    // TIME_FACTORY should be static final and instance of TypeAdapterFactory
    Field timeFactoryField = clazz.getField("TIME_FACTORY");
    assertTrue(Modifier.isStatic(timeFactoryField.getModifiers()));
    assertTrue(Modifier.isFinal(timeFactoryField.getModifiers()));
    Object timeFactoryValue = timeFactoryField.get(null);
    assertNotNull(timeFactoryValue);
    assertTrue(timeFactoryValue instanceof TypeAdapterFactory);

    // TIMESTAMP_FACTORY should be static final and instance of TypeAdapterFactory
    Field timestampFactoryField = clazz.getField("TIMESTAMP_FACTORY");
    assertTrue(Modifier.isStatic(timestampFactoryField.getModifiers()));
    assertTrue(Modifier.isFinal(timestampFactoryField.getModifiers()));
    Object timestampFactoryValue = timestampFactoryField.get(null);
    assertNotNull(timestampFactoryValue);
    assertTrue(timestampFactoryValue instanceof TypeAdapterFactory);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    var constructor = SqlTypesSupport.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    var instance = constructor.newInstance();
    assertNotNull(instance);
    assertTrue(instance instanceof SqlTypesSupport);
  }
}