package job;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.WeatherLog;
import mapper.WeatherMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import service.TeamsNotifyService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class WeatherJob implements Job {

    @Autowired
    private WeatherMapper weatherMapper;

    @Autowired
    private TeamsNotifyService teamsNotifyService;

    private static final List<City> cities = List.of(
        new City("台北市", 25.0330, 121.5654),
        new City("新北市", 25.0169, 121.4628),
        new City("桃園市", 24.9936, 121.3000)
    );

    @Override
    public void execute(JobExecutionContext context) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("🌤 **三縣市天氣更新**\n\n");

        for (City city : cities) {
            String url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_weather=true",
                city.lat, city.lon);

            try {
                String response = restTemplate.getForObject(url, String.class);
                JsonNode root = mapper.readTree(response);
                JsonNode weather = root.path("current_weather");

                double temperature = weather.get("temperature").asDouble();
                double windspeed = weather.get("windspeed").asDouble();
                LocalDateTime now = LocalDateTime.now();

                // 存資料庫
                WeatherLog log = new WeatherLog();
                log.setCity_name(city.name);
                log.setTemperature_celsius(temperature);
                log.setWind_speed_kmh(windspeed);
                log.setWeather_time(now);
                weatherMapper.insert(log);

                // 累加訊息
                messageBuilder.append(String.format(
                    "**📍 %s**\n🌡 溫度：%.1f°C\n💨 風速：%.1f km/h\n🕒 時間：%s\n\n",
                    city.name, temperature, windspeed, now
                ));

                System.out.printf("儲存成功: %s%n", city.name);

            } catch (Exception e) {
                System.err.printf("錯誤：%s -> %s%n", city.name, e.getMessage());
            }
        }

        // 傳送 Teams 訊息
        teamsNotifyService.sendMessage(messageBuilder.toString());
    }

    record City(String name, double lat, double lon) {}
}
