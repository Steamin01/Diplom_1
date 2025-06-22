package praktikum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
        verifyPrice(total, expectedTotal, burger);
    }

    @Test
    @DisplayName("Манипуляции с ингредиентами и чек")
    @Description("Проверяет добавление, перемещение, удаление ингредиентов и формат строки чека")
    public void testAddRemoveMoveAndReceipt() {
        Burger burger = createRealBurger();
        modifyIngredients(burger);
        String receipt = burger.getReceipt();
        verifyReceiptContent(receipt, burger);
    }

    @Step("Создание бургера с моком булки и ингредиентов")
    private Burger createBurgerWithMocks(float bunPrice, float[] prices) {
        Bun bun = Mockito.mock(Bun.class);
        when(bun.getPrice()).thenReturn(bunPrice);
        Burger burger = new Burger();
        burger.setBuns(bun);
        for (float price : prices) {
            Ingredient ing = Mockito.mock(Ingredient.class);
            when(ing.getPrice()).thenReturn(price);
            burger.addIngredient(ing);
        }
        return burger;
    }

    @Step("Проверка правильности расчета цены")
    private void verifyPrice(float actual, float expected, Burger burger) {
        assertEquals(expected, actual, 0.001f);
        verify(burger.bun, times(1)).getPrice();
        for (Ingredient ing : burger.ingredients) {
            verify(ing, times(1)).getPrice();
        }
    }

    @Step("Создание настоящего бургера для теста операций")
    private Burger createRealBurger() {
        Bun bun = new Bun("test bun", 10f);
        Ingredient ing1 = new Ingredient(IngredientType.FILLING, "cheese", 5f);
        Ingredient ing2 = new Ingredient(IngredientType.SAUCE, "ketchup", 2f);
        Ingredient ing3 = new Ingredient(IngredientType.FILLING, "bacon", 7f);
        Burger burger = new Burger();
        burger.setBuns(bun);
        burger.addIngredient(ing1);
        burger.addIngredient(ing2);
        burger.addIngredient(ing3);
        return burger;
    }

    @Step("Перемещение и удаление ингредиентов")
    private void modifyIngredients(Burger burger) {
        burger.moveIngredient(2, 1);
        burger.removeIngredient(2);
    }

    @Step("Проверка содержимого чека и цены")
    private void verifyReceiptContent(String receipt, Burger burger) {
        assertTrue(receipt.contains("(==== test bun ====)"));
        assertTrue(receipt.indexOf("= filling cheese =") < receipt.indexOf("= filling bacon ="));
        assertTrue(receipt.lastIndexOf("(==== test bun ====)") > receipt.indexOf("bacon"));
        String expectedPriceLine = String.format("%nPrice: %f%n", burger.getPrice());
        assertTrue(receipt.contains(expectedPriceLine));
    }
}
