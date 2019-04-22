package com.ngayngo9x.flux;

import java.util.HashMap;
import java.util.Map;

public class PipelineContainer {
    private Map<String, Object> data;

    public PipelineContainer() {
        this.data = new HashMap<>();
    }

    public void put(String name, Object value) {
        data.put(name, value);
    }

    public Object get(String name) {
        return data.get(name);
    }

}
