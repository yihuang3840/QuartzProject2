package entity;

import java.time.LocalDateTime;

public class WeatherLog {
    private String city_name;
    private Double temperature_celsius;
    private Double wind_speed_kmh;
    private LocalDateTime weather_time;
	public String getCity_name() {
		return city_name;
	}
	public Double getTemperature_celsius() {
		return temperature_celsius;
	}
	public void setTemperature_celsius(Double temperature_celsius) {
		this.temperature_celsius = temperature_celsius;
	}
	public Double getWind_speed_kmh() {
		return wind_speed_kmh;
	}
	public void setWind_speed_kmh(Double wind_speed_kmh) {
		this.wind_speed_kmh = wind_speed_kmh;
	}
	public LocalDateTime getWeather_time() {
		return weather_time;
	}
	public void setWeather_time(LocalDateTime weather_time) {
		this.weather_time = weather_time;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	
	

    
}
