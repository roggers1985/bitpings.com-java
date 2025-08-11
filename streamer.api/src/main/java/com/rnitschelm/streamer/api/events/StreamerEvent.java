package com.rnitschelm.streamer.api.events;

public sealed interface StreamerEvent permits StreamerCandleStickEvent, StreamerOrderBookEvent, StreamerTickerEvent, StreamerAggregateTradeEvent, StreamerUnknownEvent {

}
