package cyclepricing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cyclepricing.pojo.Cycle;
import cyclepricing.pojo.CycleRequest;
import cyclepricing.pojo.CycleResponse;
import cyclepricing.pojo.FinalCycleResponse;
import cyclepricing.util.CycleConstants;
import cyclepricing.util.CycleUtil;
import cyclepricing.util.PropertiesUtil;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

public class StartCycleEngine {

    private static final Properties props;
    private static final Integer MAX_THREADS_COUNT, MAX_QUEUE_CAPACITY, WAIT_TIME_FOR_PROCESSING, QUEUE_POLLING_INTERVAL;
    private static int processingSize = 1000; //default value
    private static String filePath;

    private static final Logger logger = Logger.getLogger(StartCycleEngine.class);

    static {
        BasicConfigurator.configure();

        props = PropertiesUtil.readPropertiesFile(CycleConstants.PROPS_FILE_PATH);

        MAX_THREADS_COUNT = Integer.parseInt(props.getProperty(CycleConstants.MAX_THREAD_COUNTS, "1000"));
        MAX_QUEUE_CAPACITY = Integer.parseInt(props.getProperty(CycleConstants.MAX_QUEUE_CAPACITY, "100"));
        WAIT_TIME_FOR_PROCESSING = Integer.parseInt(props.getProperty(CycleConstants.WAIT_TIME_FOR_PROCESSING, "1000"));
        QUEUE_POLLING_INTERVAL = Integer.parseInt(props.getProperty(CycleConstants.QUEUE_POLLING_INTERVAL, "5"));
    }

    private static volatile BlockingQueue<Cycle> blockingQueue = new ArrayBlockingQueue<Cycle>(MAX_QUEUE_CAPACITY);


    StartCycleEngine() {
        new Thread(() -> {
            handleRequests(filePath);
        }).start();

        new Thread(() -> {
            FinalCycleResponse finalCycleResponse = processCycles();

            try {
                String json = new ObjectMapper().writeValueAsString(finalCycleResponse);
                logger.debug("Final response " + json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public static void main(String[] args) {

        if (args.length < 1) {
            logger.error("Please mention request JSON file path!");
            System.exit(1);
        } else filePath = args[0];

        try {
            new StartCycleEngine();
        } catch (Exception e) {
            logger.error("[Instance: %s][Thread: %s] Caught Exception. \n : " + Thread.currentThread().getName());
        }

    }


    private static FinalCycleResponse processCycles() {
        FinalCycleResponse finalCycleResponse = new FinalCycleResponse();
        List<CycleResponse> cycleResponses = new ArrayList<>();
        try {
            while (true) {
                ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS_COUNT);
                List<Callable<CycleResponse>> callables = new ArrayList<>();

                if (cycleResponses.size() >= processingSize) {
                    break;
                }

                for (int i = 0; i < MAX_THREADS_COUNT; i++) {
                    callables.add(new CyclePriceEngine(blockingQueue.poll(QUEUE_POLLING_INTERVAL, TimeUnit.SECONDS)));
                }

                if (callables.size() > 0) {
                    List<Future<CycleResponse>> futures = executorService.invokeAll(callables);

                    for (Future<CycleResponse> future : futures) {
                        try {
                            CycleResponse cycleResponse = future.get();
                            if (cycleResponse != null)
                                cycleResponses.add(cycleResponse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    logger.info("Processed " + cycleResponses.size() + " cycles");
                    Thread.sleep(WAIT_TIME_FOR_PROCESSING);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finalCycleResponse.setCycleResponses(cycleResponses);
        return finalCycleResponse;
    }

    private static void handleRequests(String filePath) {


        CycleRequest cycleRequest = CycleUtil.parseJson(filePath);
        try {
            processingSize = cycleRequest.getCycles().size();
            for (Cycle cycle : cycleRequest.getCycles()) {
                blockingQueue.put(cycle);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
