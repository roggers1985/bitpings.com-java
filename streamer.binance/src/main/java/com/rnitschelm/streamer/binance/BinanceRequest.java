package com.rnitschelm.streamer.binance;

import java.util.List;

public record BinanceRequest(Object id, String method, List<String> params) {
    public BinanceRequest(final Object id, final String method, final String topic) {
        this(id, method, List.of(topic));
    }
}
