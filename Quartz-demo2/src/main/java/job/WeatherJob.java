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

@Component
public class WeatherJob implements Job {

    @Autowired
    private WeatherMapper weatherMapper;

    @Override
    public void execute(JobExecutionContext context) {
        String url = "https://api.open-meteo.com/v1/forecast?latitude=25.03&longitude=121.56&current_weather=true";

        try {
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(result);
            JsonNode current = json.get("current_weather");

            WeatherLog log = new WeatherLog();
            log.setTemperature(current.get("temperature").asDouble());
            log.setWindspeed(current.get("windspeed").asDouble());
            log.setWeatherTime(LocalDateTime.now());

            weatherMapper.insertWeatherLog(log);

            System.out.println("已寫入天氣資料：" + result);

        } catch (Exception e) {
            System.err.println("天氣 API 失敗：" + e.getMessage());
        }
    }
}
