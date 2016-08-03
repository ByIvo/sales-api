package rocks.byivo.sales.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CustomJSONMapper extends ObjectMapper {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CustomJSONMapper() {
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
