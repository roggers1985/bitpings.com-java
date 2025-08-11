package com.rnitschelm.streamer.api;

public record StreamerSymbol(String base, StreamerQuoteCurrency quote) {
//    static StreamerSymbol of(final String rawSymbol) {
//        final var symbol = rawSymbol.toLowerCase();
//
//        return Arrays.stream(StreamerQuoteCurrency.values())
//                .filter(c -> symbol.endsWith(c.name().toLowerCase()))
//                .findFirst()
//                .map(c -> new StreamerSymbol(symbol.replace(c.name().toLowerCase(), ""), c))
//                .orElseThrow(() -> new IllegalArgumentException("illegal quote received for" + symbol));
//    }
}