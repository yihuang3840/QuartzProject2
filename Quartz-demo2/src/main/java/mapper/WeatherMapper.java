package mapper;

import entity.WeatherLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

public interface WeatherMapper {

    @Insert("INSERT INTO weather_log (temperature, windspeed, weather_time) " +
            "VALUES (#{temperature}, #{windspeed}, #{weatherTime})")
    void insertWeatherLog(WeatherLog log);
}
