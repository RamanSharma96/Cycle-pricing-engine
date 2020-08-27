package cyclepricing.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import cyclepricing.pojo.CycleRequest;
import org.apache.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;

public class CycleUtil {

    private static final Logger logger = Logger.getLogger(CycleUtil.class);

    public static CycleRequest parseJson(String filePath) {

        CycleRequest cycleRequest = null;

        try {
            //read json file data to String
            byte[] jsonData = Files.readAllBytes(Paths.get(filePath));

            //create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            //convert json string to object
            cycleRequest = objectMapper.readValue(jsonData, CycleRequest.class);


        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error parsing file: " + filePath);
        }
        return cycleRequest;
    }
}
