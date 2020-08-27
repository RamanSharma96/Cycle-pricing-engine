package cyclepricing.util;

import org.junit.Test;
import org.junit.Assert;

public class CycleUtilTest {

    @Test
    public void parseJson() {
        String path = "src\\main\\resources\\sample_cycle_request.json";

        Assert.assertEquals(1000,CycleUtil.parseJson(path).getCycles().size());
    }
}
