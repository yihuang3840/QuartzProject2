package repository;

// 匯入 MyBatis 所需的註解
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import bean.WeatherBean;

public interface WeatherMapper {
  
     // 將一筆 WeatherBean 物件的資料插入到資料表 weather_log 中
     // 使用 MyBatis 的 @Insert 註解直接撰寫 SQL 語句（屬於 Annotation 方式）
     // @param log 要寫入資料庫的天氣資料物件
    @Insert("INSERT INTO weather_log (city_name, temperature_celsius, wind_speed_kmh, weather_time) " +
            "VALUES (#{city_name}, #{temperature_celsius}, #{wind_speed_kmh}, #{weather_time})")
    void insert(WeatherBean log);
}
