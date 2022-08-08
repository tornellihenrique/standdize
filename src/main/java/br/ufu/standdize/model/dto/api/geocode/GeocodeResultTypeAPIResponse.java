package br.ufu.standdize.model.dto.api.geocode;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum GeocodeResultTypeAPIResponse {
    @JsonProperty("Geography")
    GEOGRAPHY("Geography"),
    @JsonProperty("Street")
    STREET("Street"),
    @JsonProperty("Cross Street")
    CROSS_STREET("Cross Street");

    private String value;

    GeocodeResultTypeAPIResponse(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
