package ru.liger.defi.notifier.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Map;

import ru.liger.defi.notifier.integration.CoinGeckoClient;

@Service
@AllArgsConstructor
public class CryptoRateService {

    private final CoinGeckoClient coinGeckoClient;

    public Map<String, BigDecimal> getRates() {
        Map<String, Map<String, Double>> response = coinGeckoClient.getPrices("bitcoin,ethereum", "usd");

        return Map.of(
                "BTC", BigDecimal.valueOf(response.get("bitcoin").get("usd")),
                "ETH", BigDecimal.valueOf(response.get("ethereum").get("usd"))
        );
    }
}


