package service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TeamsNotifyService {

    private final String webhookUrl = "https://picmis.webhook.office.com/webhookb2/af6844ef-eb17-4960-8019-15a6baf4cd9e@dda92dc6-2169-4d9a-acae-54019f5327f2/IncomingWebhook/66c5734d92954387941a250a66a62e6a/eb4146bd-81e5-4a9b-b5db-2084c86533b4/V2jzHco56xFSL36f6RuRMom70XGH3lAwUrtnz_0cXdhr81"; // ← 改成你的 URL

    public void sendMessage(String text) {
        RestTemplate restTemplate = new RestTemplate();

        String json = String.format("{\"text\":\"%s\"}", text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(json, headers);
        restTemplate.postForEntity(webhookUrl, request, String.class);
    }
}
