package com.rnitschelm.streamer.binance;

import com.google.gson.Gson;
import com.rnitschelm.streamer.api.Streamer;
import com.rnitschelm.streamer.api.StreamerEventType;
import com.rnitschelm.streamer.api.StreamerSymbol;
import com.rnitschelm.streamer.api.events.*;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class BinanceStreamer implements Streamer {
    private final WebSocketContainer container;
    private final URI uri = new URI("wss://stream.binance.com:443/ws");
    private final Gson gson = new Gson();
    private final Map<StreamerEventType, List<Consumer<? extends StreamerEvent>>> listeners = new EnumMap<>(StreamerEventType.class);
    private final BinanceClientEndpoint endpoint = new BinanceClientEndpoint(listeners);
    private Session session;

    public BinanceStreamer(final WebSocketContainer container) throws URISyntaxException {
        this.container = container;

        this.listeners.put(StreamerEventType.TRADE, new CopyOnWriteArrayList<>());
        this.listeners.put(StreamerEventType.TICKER, new CopyOnWriteArrayList<>());
        this.listeners.put(StreamerEventType.ORDER_BOOK, new CopyOnWriteArrayList<>());
        this.listeners.put(StreamerEventType.CANDLE_STICK, new CopyOnWriteArrayList<>());
    }

    @Override
    public void connect() throws Exception {
        this.session = this.container.connectToServer(this.endpoint, this.uri);
    }

    @Override
    public void disconnect() throws Exception {
        this.session.close();
    }

    @Override
    public void subscribe(final StreamerEventType type, final StreamerSymbol symbol) {
        this.session
                .getAsyncRemote()
                .sendText(gson.toJson(
                        new BinanceRequest("1", BinanceMethod.SUBSCRIBE.toString(), getTopic(type, symbol))
                ));
    }

    @Override
    public void unsubscribe(final StreamerEventType type, final StreamerSymbol symbol) {
        this.session
                .getAsyncRemote()
                .sendText(gson.toJson(
                        new BinanceRequest("1", BinanceMethod.UNSUBSCRIBE.toString(), getTopic(type, symbol))
                ));
    }

    @Override
    public void onTrade(final Consumer<StreamerAggregateTradeEvent> onTradeReceived) {
        this.listeners.get(StreamerEventType.TRADE)
                .add(onTradeReceived);
    }

    @Override
    public void onTicker(final Consumer<StreamerTickerEvent> onTickerReceived) {
        this.listeners.get(StreamerEventType.TICKER)
                .add(onTickerReceived);
    }

    @Override
    public void onCandleStick(final Consumer<StreamerCandleStickEvent> onCandleStickReceived) {
        this.listeners.get(StreamerEventType.CANDLE_STICK)
                .add(onCandleStickReceived);
    }

    @Override
    public void onOrderBook(final Consumer<StreamerOrderBookEvent> onOrderBookReceived) {
        this.listeners.get(StreamerEventType.ORDER_BOOK)
                .add(onOrderBookReceived);
    }

    private String getTopic(final StreamerEventType type, final StreamerSymbol symbol) {
        return symbolToString(symbol).concat(switch (type) {
            case TRADE -> "@aggTrade";
            case TICKER -> "@ticker24h";
            case ORDER_BOOK -> "@depth";
            case CANDLE_STICK -> "@kline_1m";
            case UNKNOWN -> throw new IllegalArgumentException("unknown event type");
        });
    }

    private String symbolToString(final StreamerSymbol symbol) {
        return symbol.base() + symbol.quote().name().toLowerCase();
    }
}
