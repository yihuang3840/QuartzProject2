package bean;

import java.time.LocalDateTime;

// WeatherLog 類別代表一筆天氣資料的實體，包含城市名稱、攝氏溫度、風速與時間。
public class WeatherBean {
    // 城市名稱
    private String city_name;
    // 攝氏溫度
    private Double temperature_celsius;
    // 風速（公里/小時）
    private Double wind_speed_kmh;
    // 天氣紀錄時間
    private LocalDateTime weather_time;

    // 取得城市名稱
    public String getCity_name() {
        return city_name;
    }
    // 設定城市名稱
    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    // 取得攝氏溫度
    public Double getTemperature_celsius() {
        return temperature_celsius;
    }
    // 設定攝氏溫度
    public void setTemperature_celsius(Double temperature_celsius) {
        this.temperature_celsius = temperature_celsius;
    }

    // 取得風速（公里/小時）
    public Double getWind_speed_kmh() {
        return wind_speed_kmh;
    }
    // 設定風速（公里/小時）
    public void setWind_speed_kmh(Double wind_speed_kmh) {
        this.wind_speed_kmh = wind_speed_kmh;
    }

    // 取得天氣紀錄的時間
    public LocalDateTime getWeather_time() {
        return weather_time;
    }
    // 設定天氣紀錄的時間
    public void setWeather_time(LocalDateTime weather_time) {
        this.weather_time = weather_time;
    }
}
