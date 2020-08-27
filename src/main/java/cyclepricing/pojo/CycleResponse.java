package cyclepricing.pojo;

import java.util.HashMap;
import java.util.Map;

public class CycleResponse {

    private Double totalPrice;

    private Map<String, Double> componentWisePrices = new HashMap<String, Double>();

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Map<String, Double> getComponentWisePrices() {
        return componentWisePrices;
    }

    public void setComponentWisePrices(Map<String, Double> componentWisePrices) {
        this.componentWisePrices = componentWisePrices;
    }

    @Override
    public String toString() {
        return "CycleResponse{" +
                "totalPrice=" + totalPrice +
                ", componentWisePrices=" + componentWisePrices +
                '}';
    }
}
