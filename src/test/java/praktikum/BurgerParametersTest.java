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
public class BurgerParametersTest {
    private final float bunPrice;
    private final float[] ingredientPrices;
    private final float expectedTotal;

    public BurgerParametersTest(float bunPrice, float[] ingredientPrices, float expectedTotal) {
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
}
