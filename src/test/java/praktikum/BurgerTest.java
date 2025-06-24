package praktikum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class BurgerTest {
    private final float bunPrice;
    private final float[] ingredientPrices;
    private final float expectedTotal;

    public BurgerTest(float bunPrice, float[] ingredientPrices, float expectedTotal) {
        this.bunPrice = bunPrice;
        this.ingredientPrices = ingredientPrices;
        this.expectedTotal = expectedTotal;
    }

    @Parameterized.Parameters(name = "bunPrice, ingredientsPrices, expectedTotal")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {100f, new float[]{}, 200f},
                {50f, new float[]{10f}, 110f},
                {80f, new float[]{5f, 15f, 20f}, 200f}
        });
    }

    @Test
    @DisplayName("Расчет стоимости бургера с разными данными")
    @Description("Проверяет метод getPrice() для булки и ингредиентов с разными ценами")
    public void testGetPriceParameterized() {
        Burger burger = createBurgerWithMocks(bunPrice, ingredientPrices);
        float total = burger.getPrice();
        assertEquals(expectedTotal, total, 0.001f);
    }

    @Test
    @DisplayName("Добавление ингредиента")
    @Description("Проверяет, что добавленный ингредиент появляется в списке")
    public void testAddIngredient() {
        Burger burger = createBurgerWithMocks(10f, new float[]{});
        Ingredient ing = mock(Ingredient.class);
        burger.addIngredient(ing);
        assertTrue(burger.ingredients.contains(ing));
    }

    @Test
    @DisplayName("Перемещение ингредиента")
    @Description("Проверяет изменение порядка ингредиентов")
    public void testMoveIngredient() {
        Burger burger = new Burger();
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
        Burger burger = new Burger();
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

    @Step("Создание бургера с моком булки и ингредиентов для расчёта цены")
    private Burger createBurgerWithMocks(float bunPrice, float[] prices) {
        Bun bun = mock(Bun.class);
        when(bun.getPrice()).thenReturn(bunPrice);
        Burger burger = new Burger();
        burger.setBuns(bun);
        for (float price : prices) {
            Ingredient ing = mock(Ingredient.class);
            when(ing.getPrice()).thenReturn(price);
            burger.addIngredient(ing);
        }
        return burger;
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

        Ingredient ing2 = mock(Ingredient.class);
        when(ing2.getType()).thenReturn(IngredientType.SAUCE);
        when(ing2.getName()).thenReturn("ketchup");
        when(ing2.getPrice()).thenReturn(2f);

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


