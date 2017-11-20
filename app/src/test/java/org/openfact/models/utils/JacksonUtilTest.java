package org.openfact.models.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonUtilTest {

    @Test
    public void merge() {
        JsonNode node1 = JacksonUtil.toJsonNode("{\"recentContexts\":[{\"user\":\"carlos\",\"space\":null,\"document\":null}]}");
        JsonNode node2 = JacksonUtil.toJsonNode("{\"recentSpaces\":[\"sistcoop\"]}");

        JsonNode mergedJson = JacksonUtil.merge(node1, node2);

        assertThat(mergedJson).isEqualTo(JacksonUtil.toJsonNode("{\"recentContexts\": [{\"user\": \"carlos\", \"space\": null, \"document\": null}], \"recentSpaces\": [\"sistcoop\"]}"));
        assertThat(node1).isEqualTo(JacksonUtil.toJsonNode("{\"recentContexts\":[{\"user\":\"carlos\",\"space\":null,\"document\":null}]}"));
        assertThat(node2).isEqualTo(JacksonUtil.toJsonNode("{\"recentSpaces\":[\"sistcoop\"]}"));
    }

    @Test
    public void override() {
        JsonNode node1 = JacksonUtil.toJsonNode("{\"key1\": \"value1\", \"key2\": \"value2\"}");
        JsonNode node2 = JacksonUtil.toJsonNode("{\"key1\": \"newValue1\", \"key3\": \"value3\"}");

        JsonNode overridedJson = JacksonUtil.override(node1, node2);

        assertThat(overridedJson).isEqualTo(JacksonUtil.toJsonNode("{\"key1\": \"newValue1\", \"key2\": \"value2\", \"key3\": \"value3\"}"));
        assertThat(node1).isEqualTo(JacksonUtil.toJsonNode("{\"key1\": \"value1\", \"key2\": \"value2\"}"));
        assertThat(node2).isEqualTo(JacksonUtil.toJsonNode("{\"key1\": \"newValue1\", \"key3\": \"value3\"}"));
    }

}
