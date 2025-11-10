package praktikum;

import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BurgerTest {
    private Burger burger;

    @Before
    public void setUp() {
        burger = new Burger();
    }

    @Test
    @DisplayName("Добавление ингредиента")
    @Description("Проверяет, что добавленный ингредиент появляется в списке")
    public void testAddIngredient() {
        Ingredient ing = mock(Ingredient.class);
        burger.addIngredient(ing);
        assertTrue(burger.ingredients.contains(ing));
    }

    @Test
    @DisplayName("Перемещение ингредиента")
    @Description("Проверяет изменение порядка ингредиентов")
    public void testMoveIngredient() {
        Ingredient ing1 = mock(Ingredient.class);
        Ingredient ing2 = mock(Ingredient.class);
        Ingredient ing3 = mock(Ingredient.class);
        burger.addIngredient(ing1);
        burger.addIngredient(ing2);
        burger.addIngredient(ing3);
        burger.moveIngredient(2, 0);
        assertEquals(ing3, burger.ingredients.get(0));
    }

    @Test
    @DisplayName("Удаление ингредиента")
    @Description("Проверяет, что удаленный ингредиент исчезает из списка")
    public void testRemoveIngredient() {
        Ingredient ing = mock(Ingredient.class);
        burger.addIngredient(ing);
        burger.removeIngredient(0);
        assertFalse(burger.ingredients.contains(ing));
    }

    @Test
    @DisplayName("Формат чека")
    @Description("Проверяет корректный вывод чека для бургера")
    public void testGetReceipt() {
        Burger burger = createBurgerWithMocksForReceipt();
        String expected = String.format(
                "(==== %s ====)%n" +
                        "= filling cheese =%n" +
                        "= filling bacon =%n" +
                        "(==== %s ====)%n%nPrice: %f%n",
                "test bun", "test bun", burger.getPrice()
        );
        assertEquals(expected, burger.getReceipt());
    }

    @Step("Создание бургера с моками для теста чека")
    private Burger createBurgerWithMocksForReceipt() {
        Bun bun = mock(Bun.class);
        when(bun.getName()).thenReturn("test bun");
        when(bun.getPrice()).thenReturn(10f);

        Ingredient ing1 = mock(Ingredient.class);
        when(ing1.getType()).thenReturn(IngredientType.FILLING);
        when(ing1.getName()).thenReturn("cheese");
        when(ing1.getPrice()).thenReturn(5f);

        Ingredient ing3 = mock(Ingredient.class);
        when(ing3.getType()).thenReturn(IngredientType.FILLING);
        when(ing3.getName()).thenReturn("bacon");
        when(ing3.getPrice()).thenReturn(7f);

        Burger burger = new Burger();
        burger.setBuns(bun);
        burger.addIngredient(ing1);
        burger.addIngredient(ing3);
        return burger;
    }
}


