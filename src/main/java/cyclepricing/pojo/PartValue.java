package cyclepricing.pojo;

public class PartValue {

    private String partName;
    private String value;

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PartValue{" +
                "partName='" + partName + '\'' +
                ", value=" + value +
                '}';
    }
}
