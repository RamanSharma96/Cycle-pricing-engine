package cyclepricing.pojo;

import cyclepricing.pojo.Cycle;

import java.util.List;

public class CycleRequest {
    List<Cycle> cycles;

    public List<Cycle> getCycles() {
        return cycles;
    }

    public void setCycles(List<Cycle> cycles) {
        this.cycles = cycles;
    }

    @Override
    public String toString() {
        return "CycleRequest{" +
                "cycles=" + cycles +
                '}';
    }
}
