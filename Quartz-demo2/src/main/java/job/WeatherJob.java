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

import java.time.LocalDateTime;
import java.util.List;

@Component
public class WeatherJob implements Job {

    @Autowired
    private WeatherMapper weatherMapper;

    private static final List<City> cities = List.of(
        new City("台北市", 25.0330, 121.5654),
        new City("新北市", 25.0169, 121.4628),
        new City("桃園市", 24.9936, 121.3000)
    );

    @Override
    public void execute(JobExecutionContext context) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        for (City city : cities) {
            String url = String.format("https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_weather=true",
                    city.lat, city.lon);
            try {
                String response = restTemplate.getForObject(url, String.class);
                JsonNode root = mapper.readTree(response);
                JsonNode weather = root.path("current_weather");

                WeatherLog log = new WeatherLog();
                log.setCity_name(city.name);
                log.setTemperature_celsius(weather.get("temperature").asDouble());
                log.setWind_speed_kmh(weather.get("windspeed").asDouble());
                log.setWeather_time(LocalDateTime.now());

                weatherMapper.insert(log);

                System.out.println("儲存成功: " + city.name);
                System.out.println("當前溫度: " + weather.get("temperature").asDouble() + "°C");
                System.out.println("當前風速: " + weather.get("windspeed").asDouble() + " km/h");
                System.out.println("時間: " + LocalDateTime.now());
                
            } catch (Exception e) {
                System.err.println("錯誤：" + city.name + " -> " + e.getMessage());
            }
        }
    }

    record City(String name, double lat, double lon) {}
}
