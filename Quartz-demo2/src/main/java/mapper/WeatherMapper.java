package mapper;

import entity.WeatherLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WeatherMapper {
    @Insert("INSERT INTO weather_log (city_name, temperature_celsius, wind_speed_kmh, weather_time) " +
            "VALUES (#{city_name}, #{temperature_celsius}, #{wind_speed_kmh}, #{weather_time})")
    void insert(WeatherLog log);
}
