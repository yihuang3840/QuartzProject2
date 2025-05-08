package entity;

import java.time.LocalDateTime;

public class WeatherLog {
    private Double temperature;
    private Double windspeed;
    private LocalDateTime weatherTime;

    // Getter & Setter
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Double getWindspeed() { return windspeed; }
    public void setWindspeed(Double windspeed) { this.windspeed = windspeed; }

    public LocalDateTime getWeatherTime() { return weatherTime; }
    public void setWeatherTime(LocalDateTime weatherTime) { this.weatherTime = weatherTime; }
}
