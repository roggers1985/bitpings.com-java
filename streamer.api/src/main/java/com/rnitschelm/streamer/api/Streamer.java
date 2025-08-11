package com.rnitschelm.streamer.api;

import com.rnitschelm.streamer.api.events.StreamerAggregateTradeEvent;
import com.rnitschelm.streamer.api.events.StreamerCandleStickEvent;
import com.rnitschelm.streamer.api.events.StreamerOrderBookEvent;
import com.rnitschelm.streamer.api.events.StreamerTickerEvent;

import java.util.function.Consumer;

public interface Streamer {
    void connect() throws Exception;

    void disconnect() throws Exception;

    void subscribe(StreamerEventType type, StreamerSymbol symbol);

    void unsubscribe(StreamerEventType type, StreamerSymbol symbol);

    void onTrade(Consumer<StreamerAggregateTradeEvent> onTradeReceived);

    void onTicker(Consumer<StreamerTickerEvent> onTickerReceived);

    void onCandleStick(Consumer<StreamerCandleStickEvent> onCandleStickReceived);

    void onOrderBook(Consumer<StreamerOrderBookEvent> onOrderBookReceived);
}
