package cyclepricing;

import org.junit.Assert;
import org.junit.Test;

public class AbstractCyclePriceEngineTest {

    @Test
    public void checkInRangeTest() {
        String range = "1/1/2016-30/11/2016";
        String request = "15/8/2016";
        String request2 = "15/8/2020";

        Assert.assertEquals(true, AbstractCyclePriceEngine.checkInRange(range, request));
        Assert.assertEquals(false, AbstractCyclePriceEngine.checkInRange(range, request2));

    }

    @Test
    public void readFromPropsTest() {

        Double price1 = 700d, price2 = 1200d, price3 = 500d;
        Assert.assertEquals(price1, AbstractCyclePriceEngine.readFromProps("seat_material", "leather"));
        Assert.assertEquals(price2, AbstractCyclePriceEngine.readFromProps("spokes", "36"));
        Assert.assertEquals(price3, AbstractCyclePriceEngine.readFromProps("brake_type", "disc"));
    }

    @Test
    public void handleTyrePricesTest() {
        Double tyrePrice = 230d;
        String year = "1/1/2024";
        Assert.assertEquals(tyrePrice, AbstractCyclePriceEngine.handleTyrePrices(year));
    }
}
