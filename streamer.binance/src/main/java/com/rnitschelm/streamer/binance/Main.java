package com.rnitschelm.streamer.binance;

import com.rnitschelm.streamer.api.StreamerEventType;
import com.rnitschelm.streamer.api.StreamerQuoteCurrency;
import com.rnitschelm.streamer.api.StreamerSymbol;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;

import java.util.concurrent.CountDownLatch;

public class Main {
    public static void main(final String[] args) throws Exception {
        final var latch = new CountDownLatch(1);
        final var manager = new ClientManager();
        final var binanceStreamer = new BinanceStreamer(manager);

        manager
                .getProperties()
                .put(ClientProperties.RECONNECT_HANDLER, new ClientManager.ReconnectHandler());

        binanceStreamer.connect();
        binanceStreamer.subscribe(StreamerEventType.TRADE, new StreamerSymbol("btc", StreamerQuoteCurrency.USDT));
        binanceStreamer.onTrade(t -> {
            System.out.println("I just received a trade mon");
        });
        binanceStreamer.onTrade(t -> System.out.println("a real trade mun"));

        latch.await();
    }
}
