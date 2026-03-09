package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType;
import com.google.gson.TypeAdapterFactory;
import java.util.Date;

class SqlTypesSupport_502_6Test {

  @Test
    @Timeout(8000)
  void testStaticFieldsInitialization() throws Exception {
    Class<SqlTypesSupport> clazz = SqlTypesSupport.class;

    // SUPPORTS_SQL_TYPES
    Field supportsSqlTypesField = clazz.getField("SUPPORTS_SQL_TYPES");
    assertTrue(Modifier.isStatic(supportsSqlTypesField.getModifiers()));
    boolean supportsSqlTypes = supportsSqlTypesField.getBoolean(null);
    // The actual value might depend on environment; just assert field is present
    assertNotNull(supportsSqlTypesField);

    // DATE_DATE_TYPE
    Field dateDateTypeField = clazz.getField("DATE_DATE_TYPE");
    assertTrue(Modifier.isStatic(dateDateTypeField.getModifiers()));
    Object dateDateType = dateDateTypeField.get(null);
    assertNotNull(dateDateType);
    assertTrue(dateDateType instanceof DateType);

    // Use reflection to get the 'dateClass' field from DateType's superclass
    Field dateClassField = dateDateType.getClass().getSuperclass().getDeclaredField("dateClass");
    dateClassField.setAccessible(true);
    Class<?> dateClass = (Class<?>) dateClassField.get(dateDateType);
    assertTrue(Date.class.isAssignableFrom(dateClass));

    // TIMESTAMP_DATE_TYPE
    Field timestampDateTypeField = clazz.getField("TIMESTAMP_DATE_TYPE");
    assertTrue(Modifier.isStatic(timestampDateTypeField.getModifiers()));
    Object timestampDateType = timestampDateTypeField.get(null);
    assertNotNull(timestampDateType);
    assertTrue(timestampDateType instanceof DateType);

    // Use reflection to get the 'dateClass' field from DateType's superclass
    Field timestampDateClassField = timestampDateType.getClass().getSuperclass().getDeclaredField("dateClass");
    timestampDateClassField.setAccessible(true);
    Class<?> timestampDateClass = (Class<?>) timestampDateClassField.get(timestampDateType);
    // Timestamp extends Date
    assertTrue(Date.class.isAssignableFrom(timestampDateClass));

    // DATE_FACTORY
    Field dateFactoryField = clazz.getField("DATE_FACTORY");
    assertTrue(Modifier.isStatic(dateFactoryField.getModifiers()));
    Object dateFactory = dateFactoryField.get(null);
    assertNotNull(dateFactory);
    assertTrue(dateFactory instanceof TypeAdapterFactory);

    // TIME_FACTORY
    Field timeFactoryField = clazz.getField("TIME_FACTORY");
    assertTrue(Modifier.isStatic(timeFactoryField.getModifiers()));
    Object timeFactory = timeFactoryField.get(null);
    assertNotNull(timeFactory);
    assertTrue(timeFactory instanceof TypeAdapterFactory);

    // TIMESTAMP_FACTORY
    Field timestampFactoryField = clazz.getField("TIMESTAMP_FACTORY");
    assertTrue(Modifier.isStatic(timestampFactoryField.getModifiers()));
    Object timestampFactory = timestampFactoryField.get(null);
    assertNotNull(timestampFactory);
    assertTrue(timestampFactory instanceof TypeAdapterFactory);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    var constructor = SqlTypesSupport.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    // Should be able to instantiate via reflection despite private constructor
    Object instance = constructor.newInstance();
    assertNotNull(instance);
    assertTrue(instance instanceof SqlTypesSupport);
  }
}