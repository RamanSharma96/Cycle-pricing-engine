package cyclepricing;

import cyclepricing.pojo.Cycle;
import cyclepricing.pojo.CycleResponse;
import cyclepricing.pojo.PartValue;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class CyclePriceEngine extends AbstractCyclePriceEngine implements Callable<CycleResponse> {

    private Cycle cycle;
    private static Logger logger = Logger.getLogger(CyclePriceEngine.class);

    CyclePriceEngine(Cycle cycle) {
        this.cycle = cycle;
    }

    private CycleResponse process() {

        CycleResponse cycleResponse = null;

        Map<String, Double> componentWisePrices = new HashMap<>();
        Double totalPrice = 0d;

        try {
            if (cycle != null && cycle.getParts().size() > 0) {
                logger.debug("Processing cycle " + cycle.toString());

                cycleResponse = new CycleResponse();
                for (Map.Entry<String, List<PartValue>> partsMap : cycle.getParts().entrySet()) {
                    String highLevelPart = partsMap.getKey();
                    List<PartValue> specificParts = partsMap.getValue();
                    Double totalPricePerPart = 0d;

                    for (PartValue part : specificParts) {
                        String specificPart = part.getPartName();

                        Double price = readFromProps(specificPart, part.getValue());
                        logger.debug("Adding price : " + price + " for part " + specificPart);

                        if (price != null)
                            totalPricePerPart += price;
                        else {
                            logger.error("No such part " + specificPart + " for value " + part.getValue());
                            return null;
                        }
                    }
                    componentWisePrices.put(highLevelPart, totalPricePerPart);
                    totalPrice += totalPricePerPart;
                }

                cycleResponse.setComponentWisePrices(componentWisePrices);
                cycleResponse.setTotalPrice(totalPrice);
            }
        } catch (Exception e) {
            logger.error("Error processing cycle " + e);
        }

        return cycleResponse;
    }

    public CycleResponse call() throws Exception {
        return process();
    }
}
