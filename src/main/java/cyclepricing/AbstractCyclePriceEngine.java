package cyclepricing;

import cyclepricing.util.CycleConstants;
import cyclepricing.util.PropertiesUtil;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AbstractCyclePriceEngine {

    private static final String DEFAULT_PARTS = "frame,handle,seat,wheel,chain_assembly";
    private static Properties props = PropertiesUtil.readPropertiesFile(CycleConstants.PROPS_FILE_PATH);
    private static HashMap<String, HashMap<String, Double>> partsValues = new HashMap<>();
    private static HashMap<String, Double> tyresPrices = new HashMap<>();

    private static final Logger logger = Logger.getLogger(AbstractCyclePriceEngine.class);

    static {
        logger.info("Reading from properties file.");

        String[] highLevelParts = props.getProperty(CycleConstants.CYCLE_PARTS, DEFAULT_PARTS).split(",");

        for (String cyclePart : highLevelParts) {
            String[] parts = props.getProperty(cyclePart + "_properties").split(",");

            for (String part : parts) {
                HashMap<String, Double> partPricesMap = new HashMap<>();
                String[] partPrices = props.getProperty(part).split(",");
                for (String partPrice : partPrices) {
                    String[] partPriceArr = partPrice.split(":");
                    partPricesMap.put(partPriceArr[0], Double.parseDouble(partPriceArr[1]));
                }
                partsValues.put(part, partPricesMap);
            }
        }

        String mfgPrices = props.getProperty(CycleConstants.TYRE_MANUFACTURE_DATE);

        for (String mfg : mfgPrices.split(",")) {
            String[] prices = mfg.split(":");
            Double price = Double.parseDouble(prices[1]);
            tyresPrices.put(prices[0], price);
        }
    }

    public static Double readFromProps(String specificPart, String specificPartValue) {

        if (CycleConstants.TYRE_MANUFACTURE_DATE.equalsIgnoreCase(specificPart)) {
            return handleTyrePrices(specificPartValue);
        } else if (partsValues.containsKey(specificPart) && partsValues.get(specificPart).containsKey(specificPartValue)) {
            return partsValues.get(specificPart).get(specificPartValue);
        } else return null;
    }

    public static Double handleTyrePrices(String mfgYear) {
        Double tyrePrice = null;
        for (Map.Entry<String, Double> entry : tyresPrices.entrySet()) {
            String tyreRanges = entry.getKey();

            if (checkInRange(tyreRanges, mfgYear)) {
                tyrePrice = entry.getValue();
            }
        }

        return tyrePrice;
    }

    public static boolean checkInRange(String range, String requested) {
        boolean isInRange = false;
        Date startDate, endDate;
        String[] dates = range.split("-");
        try {
            Date requestedDate = new SimpleDateFormat("dd/MM/yyyy").parse(requested);
            startDate = new SimpleDateFormat("dd/MM/yyyy").parse(dates[0]);

            if (dates.length > 1) {
                endDate = new SimpleDateFormat("dd/MM/yyyy").parse(dates[1]);

                isInRange = startDate.before(requestedDate) && endDate.after(requestedDate);

            } else {
                isInRange = startDate.before(requestedDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isInRange;
    }
}

