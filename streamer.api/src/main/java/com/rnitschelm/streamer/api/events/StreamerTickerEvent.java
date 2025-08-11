package com.rnitschelm.streamer.api.events;

import com.rnitschelm.streamer.api.StreamerSymbol;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public final class StreamerTickerEvent implements StreamerEvent {
    private StreamerSymbol symbol;
    private BigDecimal bestBidPrice;
    private BigDecimal bestBidSize;
    private BigDecimal bestAskPrice;
    private BigDecimal bestAskSize;
    private LocalDateTime timestamp;
}
