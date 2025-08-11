package com.rnitschelm.streamer.binance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.rnitschelm.streamer.api.StreamerSymbol;
import com.rnitschelm.streamer.api.events.StreamerAggregateTradeEvent;
import com.rnitschelm.streamer.api.events.StreamerEvent;
import com.rnitschelm.streamer.api.events.StreamerTickerEvent;
import com.rnitschelm.streamer.api.events.StreamerUnknownEvent;
import com.rnitschelm.streamer.binance.adapters.BinanceSymbolAdapter;
import com.rnitschelm.streamer.binance.adapters.BinanceTickerAdapter;
import com.rnitschelm.streamer.binance.adapters.BinanceTradeAdapter;

import java.util.Optional;

public class BinanceParser {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(StreamerSymbol.class, new BinanceSymbolAdapter())
            .registerTypeAdapter(StreamerAggregateTradeEvent.class, new BinanceTradeAdapter())
            .registerTypeAdapter(StreamerTickerEvent.class, new BinanceTickerAdapter())
            .create();

    public StreamerEvent parse(final String json) {
        final var jsonElement = JsonParser.parseString(json);

        if (!jsonElement.isJsonObject()) {
            return new StreamerUnknownEvent();
        }

        final var jsonObject = jsonElement.getAsJsonObject();
        final var type = Optional.ofNullable(jsonObject.get("e"));

        return type.map(t ->
                switch (t.getAsString()) {
                    case "aggTrade" -> gson.fromJson(json, StreamerAggregateTradeEvent.class);
                    case "ticker24h" -> gson.fromJson(json, StreamerTickerEvent.class);
                    default -> new StreamerUnknownEvent();
                }
        ).orElse(new StreamerUnknownEvent());
    }
}
