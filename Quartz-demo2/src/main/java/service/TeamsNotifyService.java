package service;

// Spring 註解，將此類別標記為 Service 層，會被 Spring 掃描並註冊成 Bean
import org.springframework.stereotype.Service;

// WebClient 是 Spring WebFlux 提供的非同步 HTTP 客戶端
import org.springframework.web.reactive.function.client.WebClient;

@Service // 標記此類別為 Spring 的 Service，讓其他類別可以透過 @Autowired 使用
public class TeamsNotifyService {

    // 建立 WebClient 實例（可重複使用）
    // WebClient 預設會使用非同步、非阻塞 I/O 模型來發送 HTTP 請求
    private final WebClient webClient = WebClient.create();

    // Microsoft Teams Webhook URL（請使用你自己頻道的 webhook）
    private final String webhookUrl = "https://picmis.webhook.office.com/webhookb2/063e4e34-c438-4214-8b95-e55410cf77da@dda92dc6-2169-4d9a-acae-54019f5327f2/IncomingWebhook/9b07b0ae562c4152a3f40ac539c43452/eb4146bd-81e5-4a9b-b5db-2084c86533b4/V2eTwDO7B3D7c43vGe4dBA91hHrCzkuzSY870JrgvS5MI1";

    // 傳送訊息到 Microsoft Teams（非同步方式）
    // @param text 要傳送的文字內容（會以 JSON 格式傳送給 Teams）
    public void sendMessage(String text) {
        // 建立 JSON 格式的 資料內容，例如 {"text": "Hello from WebClient!"}
        String jsonPayload = String.format("{\"text\":\"%s\"}", text);

        // 使用 WebClient 發送 HTTP POST 請求
        webClient.post()
                .uri(webhookUrl)                           // 目標 URI 為 Teams 的 Webhook URL
                .header("Content-Type", "application/json") // 設定 Content-Type 為 JSON
                .bodyValue(jsonPayload)                    // 設定請求的 JSON 主體
                .retrieve()                                // 發送請求並等待回應
                .bodyToMono(String.class)                  // 將回應內容轉為 Mono<String>
                .doOnSuccess(response ->
                    System.out.println("Teams 傳送成功")     // 傳送成功時印出成功訊息
                )
                .doOnError(error ->
                    System.err.println("傳送失敗: " + error.getMessage()) // 發生錯誤時印出錯誤訊息
                )
                .subscribe(); // 非同步觸發執行，立即返回，不阻塞主執行緒
    }
}
