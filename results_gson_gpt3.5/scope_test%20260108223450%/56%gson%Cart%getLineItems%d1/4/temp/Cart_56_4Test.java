package com.google.gson.examples.android.model;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import com.google.gson.annotations.SerializedName;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Constructor;

class Cart_56_4Test {

  @Test
    @Timeout(8000)
  void testGetLineItems_returnsLineItemsList() throws Exception {
    List<LineItem> items = new ArrayList<>();
    items.add(new LineItem("item1", 2));
    items.add(new LineItem("item2", 5));

    // Use reflection to instantiate Cart since Cart class is not directly accessible
    ClassLoader classLoader = Cart.class.getClassLoader();
    Class<?> cartClass = classLoader.loadClass("com.google.gson.examples.android.model.Cart");
    Constructor<?> constructor = cartClass.getDeclaredConstructor(List.class, String.class, String.class);
    constructor.setAccessible(true);
    Object cart = constructor.newInstance(items, "buyerName", "1234-5678-9012-3456");

    // Invoke getLineItems method via reflection
    @SuppressWarnings("unchecked")
    List<Object> result = (List<Object>) cartClass.getMethod("getLineItems").invoke(cart);

    assertNotNull(result);
    assertEquals(2, result.size());

    Object item0 = result.get(0);
    Object item1 = result.get(1);

    // Use reflection to access getName and getQuantity on LineItem
    Class<?> lineItemClass = classLoader.loadClass("com.google.gson.examples.android.model.CartTest$LineItem");
    var getNameMethod = item0.getClass().getMethod("getName");
    var getQuantityMethod = item0.getClass().getMethod("getQuantity");

    assertEquals("item1", getNameMethod.invoke(item0));
    assertEquals(2, getQuantityMethod.invoke(item0));
    assertEquals("item2", getNameMethod.invoke(item1));
    assertEquals(5, getQuantityMethod.invoke(item1));
  }

  // Minimal LineItem class for testing
  public static class LineItem {
    private final String name;
    private final int quantity;

    public LineItem(String name, int quantity) {
      this.name = name;
      this.quantity = quantity;
    }

    public String getName() {
      return name;
    }

    public int getQuantity() {
      return quantity;
    }
  }
}