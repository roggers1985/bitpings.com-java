package com.rnitschelm.streamer.api.events;

import com.rnitschelm.streamer.api.StreamerSymbol;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public final class StreamerAggregateTradeEvent implements StreamerEvent {
    private long id;
    private LocalDateTime timestamp;
    private StreamerSymbol symbol;
    private BigDecimal price;
    private BigDecimal quantity;
}
