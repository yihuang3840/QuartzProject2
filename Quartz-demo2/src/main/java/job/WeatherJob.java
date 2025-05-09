package job;

// Jackson：用來處理 JSON 格式
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import bean.WeatherBean;
import repository.WeatherMapper;

// Quartz：排程框架所需介面
import org.quartz.Job;
import org.quartz.JobExecutionContext;

// Spring：用來進行元件註冊與依賴注入
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 用來發送 HTTP 請求
import org.springframework.web.client.RestTemplate;

// Teams 通知服務
import service.TeamsNotifyService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component // 將此類別註冊為 Spring 元件，讓 Spring 能進行管理與注入
public class WeatherJob implements Job {

    @Autowired // 自動注入 MyBatis 的 WeatherMapper，負責將資料寫入資料庫
    private WeatherMapper weatherMapper;

    @Autowired // 自動注入 Microsoft Teams 的通知服務
    private TeamsNotifyService teamsNotifyService;

    // 定義三個城市及其經緯度
 // 定義城市及其經緯度
    private static final List<City> cities = List.of(
    	new City("基隆市", 25.1283, 121.7419),
        new City("台北市", 25.0330, 121.5654),
        new City("新北市", 25.0169, 121.4628),
        new City("桃園市", 24.9936, 121.3000),
        new City("新竹市", 24.8039, 120.9647),
        new City("新竹縣", 24.8385, 121.0020)
    );


    // Quartz 執行排程時會自動呼叫此方法
    @Override
    public void execute(JobExecutionContext context) {
        RestTemplate restTemplate = new RestTemplate(); // 用來發送 API 請求
        ObjectMapper mapper = new ObjectMapper();       // 解析 JSON 回應
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"); // 格式化時間用

        StringBuilder messageBuilder = new StringBuilder(); // Teams 訊息累積器
        messageBuilder.append("🌤 **三縣市天氣更新**\n\n");

        // 針對每個城市做 API 呼叫、資料儲存與訊息拼接
        for (City city : cities) {
            String url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_weather=true",
                city.lat, city.lon);

            try {
                // 發送 HTTP GET 請求，取得 JSON 字串
                String response = restTemplate.getForObject(url, String.class);
                JsonNode root = mapper.readTree(response); // 將字串轉成 JSON 樹狀結構
                JsonNode weather = root.path("current_weather"); // 取得當前天氣資訊節點

                double temperature = weather.get("temperature").asDouble(); // 攝氏溫度
                double windspeed = weather.get("windspeed").asDouble();     // 風速
                LocalDateTime now = LocalDateTime.now();                    // 現在時間

                // 建立一筆天氣紀錄並存入資料庫
                WeatherBean log = new WeatherBean();
                log.setCity_name(city.name);
                log.setTemperature_celsius(temperature);
                log.setWind_speed_kmh(windspeed);
                log.setWeather_time(now);
                weatherMapper.insert(log); // 透過 MyBatis 寫入資料庫

                // 建立每個城市的訊息內容
                messageBuilder.append(String.format(
                    "**| %s**\n   |溫度：%.1f°C\n   |風速：%.1f km/h\n   |時間：%s\n\n",
                    city.name, temperature, windspeed, now.format(formatter)
                ));

                System.out.printf("儲存成功: %s%n", city.name); // 印出成功訊息

            } catch (Exception e) {
                System.err.printf("錯誤：%s -> %s%n", city.name, e.getMessage()); // 印出錯誤訊息
            }
        }

        // 將組合好的訊息送到 Teams
        teamsNotifyService.sendMessage(messageBuilder.toString());
    }

    // Java 14+ 的 record 類別，用來簡單封裝城市名稱與經緯度
    record City(String name, double lat, double lon) {}
}
