package cyclepricing.pojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cycle {

    Map<String, List<PartValue>> parts = new HashMap<>();

    public Map<String, List<PartValue>> getParts() {
        return parts;
    }

    public void setParts(Map<String, List<PartValue>> parts) {
        this.parts = parts;
    }

    @Override
    public String toString() {
        return "Cycle{" +
                "parts=" + parts +
                '}';
    }
}
