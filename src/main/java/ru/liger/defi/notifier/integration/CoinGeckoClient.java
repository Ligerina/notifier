package ru.liger.defi.notifier.integration;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "coinGeckoClient", url = "https://api.coingecko.com")
public interface CoinGeckoClient {

    @GetMapping("/api/v3/simple/price")
    Map<String, Map<String, Double>> getPrices(
            @RequestParam("ids") String ids,
            @RequestParam("vs_currencies") String vsCurrencies
    );
}

