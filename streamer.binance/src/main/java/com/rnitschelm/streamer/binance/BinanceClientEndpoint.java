package com.rnitschelm.streamer.binance;

import com.rnitschelm.streamer.api.StreamerEventType;
import com.rnitschelm.streamer.api.events.*;
import jakarta.websocket.*;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@ClientEndpoint
public class BinanceClientEndpoint {
    private final BinanceParser parser = new BinanceParser();
    private final Map<StreamerEventType, List<Consumer<? extends StreamerEvent>>> listeners;

    public BinanceClientEndpoint(final Map<StreamerEventType, List<Consumer<? extends StreamerEvent>>> listeners) {
        this.listeners = listeners;
    }

    @OnMessage
    public void onMessage(final String message) {
        this.runListeners(this.parser.parse(message));
    }

    @OnClose
    public void onClose() {
    }

    @OnOpen
    public void onOpen() {
        System.out.println("open");
    }

    @OnError
    public void onError(final Throwable throwable) {
        System.out.println(throwable);
    }

    @SuppressWarnings("unchecked")
    private void runListeners(final StreamerEvent event) {
        switch (event) {
            case final StreamerTickerEvent e -> listeners
                    .get(StreamerEventType.TICKER)
                    .forEach(l -> ((Consumer<StreamerTickerEvent>) l).accept(e));
            case final StreamerCandleStickEvent e -> listeners
                    .get(StreamerEventType.CANDLE_STICK)
                    .forEach(l -> ((Consumer<StreamerCandleStickEvent>) l).accept(e));
            case final StreamerAggregateTradeEvent e -> listeners
                    .get(StreamerEventType.TRADE)
                    .forEach(l -> ((Consumer<StreamerAggregateTradeEvent>) l).accept(e));
            case final StreamerOrderBookEvent e -> listeners
                    .get(StreamerEventType.ORDER_BOOK)
                    .forEach(l -> ((Consumer<StreamerOrderBookEvent>) l).accept(e));
            case final StreamerUnknownEvent u -> {
            }
        }
        ;
    }
}
