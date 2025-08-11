package com.rnitschelm.streamer.binance.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.rnitschelm.streamer.api.events.StreamerTickerEvent;

import java.io.IOException;

public class BinanceTickerAdapter extends TypeAdapter<StreamerTickerEvent> {
    @Override
    public void write(final JsonWriter jsonWriter, final StreamerTickerEvent streamerTickerEvent) throws IOException {

    }

    @Override
    public StreamerTickerEvent read(final JsonReader jsonReader) throws IOException {
        return null;
    }
}
