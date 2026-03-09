package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ReflectionAccessFilterHelper_101_6Test {

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withAndroidType() throws Exception {
    // Use a dummy class with name starting with "android."
    Class<?> androidLikeClass = createDummyClassWithName("android.app.Activity");
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(androidLikeClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withKotlinType() throws Exception {
    // Use reflection to get kotlin.Unit class to avoid compile error
    Class<?> kotlinClass;
    try {
      kotlinClass = Class.forName("kotlin.Unit");
    } catch (ClassNotFoundException e) {
      // If kotlin.Unit class is not available in classpath, skip test
      return;
    }
    // kotlin.Unit name starts with "kotlin."
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(kotlinClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withKotlinxType() throws Exception {
    // Use a dummy class with name starting with "kotlinx."
    Class<?> kotlinxLikeClass = createDummyClassWithName("kotlinx.coroutines.CoroutineScope");
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(kotlinxLikeClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withScalaType() throws Exception {
    // Use a dummy class with name starting with "scala."
    Class<?> scalaLikeClass = createDummyClassWithName("scala.collection.Seq$");
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(scalaLikeClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withNonPlatformType() {
    assertFalse(ReflectionAccessFilterHelper.isAnyPlatformType(String.class));
  }

  /**
   * Creates a dummy class with the specified fully qualified name using a minimal valid class byte array.
   */
  private static Class<?> createDummyClassWithName(String className) throws Exception {
    byte[] classBytes = generateMinimalClassBytes(className);
    DummyClassLoader loader = new DummyClassLoader();
    return loader.defineClassForTest(className, classBytes);
  }

  // Minimal class bytecode generator for a public class with no members, fixed LineNumberTable attribute length.
  private static byte[] generateMinimalClassBytes(String className) throws Exception {
    String internalName = className.replace('.', '/');
    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
    java.io.DataOutputStream dos = new java.io.DataOutputStream(baos);

    dos.writeInt(0xCAFEBABE); // magic
    dos.writeInt(0x00000034); // version: 52 (Java 8)

    // Constant pool count: 13 (indexing starts at 1)
    dos.writeShort(13);

    // 1: Utf8 class name
    dos.writeByte(1);
    dos.writeUTF(internalName);

    // 2: Class #1
    dos.writeByte(7);
    dos.writeShort(1);

    // 3: Utf8 superclass name "java/lang/Object"
    dos.writeByte(1);
    dos.writeUTF("java/lang/Object");

    // 4: Class #3
    dos.writeByte(7);
    dos.writeShort(3);

    // 5: Utf8 "<init>"
    dos.writeByte(1);
    dos.writeUTF("<init>");

    // 6: Utf8 "()V"
    dos.writeByte(1);
    dos.writeUTF("()V");

    // 7: Utf8 "Code"
    dos.writeByte(1);
    dos.writeUTF("Code");

    // 8: NameAndType #5:#6
    dos.writeByte(12);
    dos.writeShort(5);
    dos.writeShort(6);

    // 9: Methodref #4.#8
    dos.writeByte(10);
    dos.writeShort(4);
    dos.writeShort(8);

    // 10: Utf8 "LineNumberTable"
    dos.writeByte(1);
    dos.writeUTF("LineNumberTable");

    // 11: Utf8 "SourceFile"
    dos.writeByte(1);
    dos.writeUTF("SourceFile");

    // 12: Utf8 "Dummy.java"
    dos.writeByte(1);
    dos.writeUTF("Dummy.java");

    // Access flags: public super
    dos.writeShort(0x0021);

    // This class: #2
    dos.writeShort(2);

    // Super class: #4
    dos.writeShort(4);

    // Interfaces count: 0
    dos.writeShort(0);

    // Fields count: 0
    dos.writeShort(0);

    // Methods count: 1
    dos.writeShort(1);

    // Method: public <init>()V
    dos.writeShort(0x0001); // access flags: public
    dos.writeShort(5); // name_index: <init>
    dos.writeShort(6); // descriptor_index: ()V
    dos.writeShort(1); // attributes_count

    // Attribute: Code
    dos.writeShort(7); // attribute_name_index: Code
    dos.writeInt(17); // attribute_length

    dos.writeShort(1); // max_stack
    dos.writeShort(1); // max_locals
    dos.writeInt(5);   // code_length

    // bytecode:
    // 0: aload_0
    // 1: invokespecial java/lang/Object.<init> ()V
    // 4: return
    dos.writeByte(0x2A); // aload_0
    dos.writeByte(0xB7); // invokespecial
    dos.writeShort(9);   // methodref #9
    dos.writeByte(0xB1); // return

    dos.writeShort(0); // exception_table_length
    dos.writeShort(1); // attributes_count

    // LineNumberTable attribute
    dos.writeShort(10); // attribute_name_index: LineNumberTable
    dos.writeInt(4);    // attribute_length (corrected to 4 bytes)
    dos.writeShort(0);  // start_pc
    dos.writeShort(1);  // line_number

    // Class attributes count: 1
    dos.writeShort(1);

    // SourceFile attribute
    dos.writeShort(11); // attribute_name_index: SourceFile
    dos.writeInt(2);    // attribute_length
    dos.writeShort(12); // sourcefile_index: Dummy.java

    dos.flush();
    return baos.toByteArray();
  }

  // Custom ClassLoader to define classes from byte arrays
  static class DummyClassLoader extends ClassLoader {
    public Class<?> defineClassForTest(String name, byte[] bytes) {
      return defineClass(name, bytes, 0, bytes.length);
    }
  }
}