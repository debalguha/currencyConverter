package au.com.infographi.sc;

import java.util.HashMap;
import java.util.Map;

public class DataMapBuilder {
    private Map<String, Object> dataMap;

    public static DataMapBuilder withHashMapInstance(){
        DataMapBuilder builder = new DataMapBuilder();
        builder.dataMap = new HashMap<>();
        return builder;
    }
    public DataMapBuilder withKeyAndValue(String key, Object value) {
        this.dataMap.put(key, value);
        return this;
    }

    public Map<String, Object> build() {
        return dataMap;
    }
}
