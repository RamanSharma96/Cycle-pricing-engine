package cyclepricing.pojo;

import cyclepricing.pojo.CycleResponse;

import java.util.ArrayList;
import java.util.List;

public class FinalCycleResponse {

    List<CycleResponse> cycleResponses = new ArrayList<CycleResponse>();

    public List<CycleResponse> getCycleResponses() {
        return cycleResponses;
    }

    public void setCycleResponses(List<CycleResponse> cycleResponses) {
        this.cycleResponses = cycleResponses;
    }

    @Override
    public String toString() {
        return "FinalCycleResponse{" +
                "cycleResponses=" + cycleResponses +
                '}';
    }
}
