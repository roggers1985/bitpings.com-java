package com.rnitschelm.streamer.binance.adapters;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.rnitschelm.streamer.api.StreamerQuoteCurrency;
import com.rnitschelm.streamer.api.StreamerSymbol;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BinanceSymbolAdapter extends TypeAdapter<StreamerSymbol> {
    private final List<String> quotes = Arrays
            .stream(StreamerQuoteCurrency.values()).map(StreamerQuoteCurrency::name)
            .sorted((a, b) -> a.length() - b.length())
            .toList();

    @Override
    public void write(final JsonWriter out, final StreamerSymbol streamerSymbol) throws IOException {
        final var symbol = (streamerSymbol.base() + streamerSymbol.quote().name()).toLowerCase();
        out.value(symbol);
    }

    @Override
    public StreamerSymbol read(final JsonReader in) throws IOException {
        final var symbol = in.nextString();

        return quotes
                .stream()
                .filter(symbol::endsWith)
                .findFirst()
                .map(q -> new StreamerSymbol(symbol.replace(q, ""), StreamerQuoteCurrency.valueOf(q)))
                .orElseThrow(() -> new JsonParseException("invalid symbol received"));
    }
}
