package com.rnitschelm.streamer.binance.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.rnitschelm.streamer.api.events.StreamerAggregateTradeEvent;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;

public class BinanceTradeAdapter extends TypeAdapter<StreamerAggregateTradeEvent> {
    @Override
    public void write(final JsonWriter out, final StreamerAggregateTradeEvent streamerTradeEvent) throws IOException {
        throw new RuntimeException("write does not have to be implemented");
    }

    @Override
    public StreamerAggregateTradeEvent read(final JsonReader in) throws IOException {
        final var tradeEvent = new StreamerAggregateTradeEvent();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "p" -> tradeEvent.setPrice(new BigDecimal(in.nextString()));
                case "T" -> {
                    final var instant = Instant.ofEpochMilli(in.nextLong());
                    final var zoneId = ZoneId.systemDefault();
                    final var timestamp = instant.atZone(zoneId).toLocalDateTime();
                    tradeEvent.setTimestamp(timestamp);
                }
                case "q" -> tradeEvent.setQuantity(new BigDecimal(in.nextString()));
                case "s" -> tradeEvent.setSymbol(new BinanceSymbolAdapter().read(in));
                case "t" -> tradeEvent.setId(in.nextLong());
                default -> in.skipValue();
            }
        }
        in.endObject();

        return tradeEvent;
    }
}
