package com.weather.handler.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.entity.message.TaskStatusMsg;
import com.weather.entity.table.MeteoData;
import com.weather.mapper.SaveToMySQLMapper;
import com.weather.repository.RedisRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
@AllArgsConstructor
public class ResponseHandlerImpl implements ResponseHandler {
    private final RedisTemplate<String, String> redisTemplate;
    private final SaveToMySQLMapper mapper;
    private final RabbitTemplate rabbitTemplate;
    private final RedisRepository repository;

    @SneakyThrows
    @Override
    public void saveToken(String username, String token) {
        String key = "tokens";
        redisTemplate.opsForHash().put(key, username, token);
        String savedToken = (String) redisTemplate.opsForHash().get(key, username);
        log.info("Token saved to Redis: " + savedToken);
        rabbitTemplate
                .convertAndSend("task-over-exchange", "task-over-routing-key",
                        new ObjectMapper().writeValueAsString(new TaskStatusMsg("saveToken")));
    }

    @Override
    public void deleteToken(String token) {
        String key = "token:" + token;
        redisTemplate.delete(key);
        log.info("The token '" + token + "' stored in Redis has been deleted");
    }

    @SneakyThrows
    @Override
    public void saveAllStationCode(String data) {
        List<String> dataList = new ArrayList<>();
        int startPos = data.indexOf("[{");
        int endPos = data.lastIndexOf("}]");
        if (startPos >= 0 && endPos >= 0) {
            String[] stationData = data.substring(startPos + 2, endPos).split("\\},\\{");
            for (String station : stationData) {
                Map<String, String> stationMap = new HashMap<>();
                String[] keyValuePairs = station.split(",");
                for (String pair : keyValuePairs) {
                    String[] parts = pair.split(":");
                    if (parts.length == 2) {
                        String key = parts[0].replaceAll("\"", "").trim();
                        String value = parts[1].replaceAll("\"", "").trim();
                        stationMap.put(key, value);
                    }
                }
                dataList.add(stationMap.get("station") + "," + stationMap.get("name"));
            }
        }
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
        for (int i = 0; i < dataList.size(); i++) {
            String stationData = dataList.get(i);
            String[] parts = stationData.split(",");
            if (parts.length == 2) {
                String stationKey = parts[0];
                String stationName = parts[1];
                ops.put("allStationCode:station&name", stationKey, stationName);
                mapper.insertStation(stationKey, stationName);
            }
        }
        log.info("The stationCode has been successfully saved to Redis");
        rabbitTemplate
                .convertAndSend("task-over-exchange", "task-over-routing-key",
                        new ObjectMapper().writeValueAsString(new TaskStatusMsg("saveStationCode")));
    }

    @SneakyThrows
    @Override
    public void saveMeteoDateRange(String d_station, String meteoDateRange) {
        List<String> meteoDateRangeList = new ArrayList<>();
        String[] meteoDateRangeData = meteoDateRange.split(",");
        for (String date : meteoDateRangeData) {
            String saveDate = date.replaceAll("\"", "").trim().replaceAll("\\]", "").trim();
            meteoDateRangeList.add(saveDate.replaceAll("\\[", "").trim());
        }
        for (int i = 0; i < meteoDateRangeList.size(); i++) {
            String date = meteoDateRangeList.get(i);
            redisTemplate.opsForSet().add(d_station + ":dateRange", date);
            mapper.insertStationDateRange(date, d_station);
        }
        log.info("The MeteoDateRange has been successfully saved to Redis");
        rabbitTemplate
                .convertAndSend("task-over-exchange", "task-over-routing-key",
                        new ObjectMapper().writeValueAsString(new TaskStatusMsg("saveDateRange")));
    }


    @SneakyThrows
    @Override
    public void saveMeteoData(int last, String station, String date, String data) {
        List<List<String>> dataList = new ArrayList<>();
        int startPos = data.indexOf("[[");
        int endPos = data.lastIndexOf("]]");
        if (startPos >= 0 && endPos >= 0) {
            String[] dataArray = data.substring(startPos + 2, endPos).split("\\],\\[");
            for (String item : dataArray) {
                item = item.replaceAll("\\[|\\]", "");
                String[] values = item.split(",");
                dataList.add(Arrays.asList(values));
            }
        }
        ZSetOperations<String, String> ops = redisTemplate.opsForZSet();
        for (int i = 0; i < dataList.size(); i++) {
            ops.add(String.format("%s_data_%s", station, date),
                    dataList.get(i).toString(),
                    ZonedDateTime.of(
                                    LocalDateTime
                                            .parse(date.substring(1, 11) + " " + dataList.get(i).get(0).substring(1, 9),
                                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                                    , ZoneId.of("Asia/Shanghai"))
                            .toEpochSecond());
        }
        if (last == 1){
            RedisScript<String> rdbSaveLua = new DefaultRedisScript<>(rdbLuaScript(), String.class);
            String result = redisTemplate.execute(rdbSaveLua, Collections.singletonList(String.format("%s_data_%s", station, date)));
            System.out.println(result);
            saveMeteoToMySQL(station, date);
        }
    }

    @SneakyThrows
    public void saveMeteoToMySQL(String station, String date) {
        Set<ZSetOperations.TypedTuple<String>> meteo = repository.getMeteoData(station, date);
        List<MeteoData> meteoList = new ArrayList<>();
        for (ZSetOperations.TypedTuple<String> tuple : meteo) {
            List<String> value = Arrays.asList(tuple.getValue().replaceAll("\\[|\\]", "").split(","));
            meteoList.add(new MeteoData(
                    station,
                    date,
                    new Date((long) (tuple.getScore() * 1000)),
                    Time.valueOf(value.get(0).replace("\"", "")),
                    Float.parseFloat(value.get(1).replace("\"", "")),
                    Float.parseFloat(value.get(2).replace("\"", "")),
                    Float.parseFloat(value.get(3).replace("\"", "")),
                    Float.parseFloat(value.get(4).replace("\"", "")),
                    Float.parseFloat(value.get(5).replace("\"", "")),
                    Float.parseFloat(value.get(6).replace("\"", "")),
                    Float.parseFloat(value.get(7).replace("\"", "")),
                    Float.parseFloat(value.get(8).replace("\"", ""))
            ));
        }
        if (mapper.insertMeteoData(String.format(station + "_meteo_data"), meteoList) > 0) {
            rabbitTemplate
                    .convertAndSend("task-over-exchange", "task-over-routing-key",
                            new ObjectMapper().writeValueAsString(new TaskStatusMsg("saveMeteoData")));
        }
    }

    public String rdbLuaScript(){
        return "local keys = redis.call(\"KEYS\", \"*\")\n" +
                "local use_key = ARGV[1]\n" +
                "for i, key in ipairs(keys) do\n" +
                "    redis.call(\"SELECT\", 1)\n" +
                "    if redis.call(\"EXISTS\", key) == use_key then\n" +
                "        redis.call(\"SAVE\")\n" +
                "        local old_filename = \"/meteo/redis/data/dump.rdb\"\n" +
                "        local new_filename = \"/meteo/redis/data/share/\" .. use_key .. \".rdb\"\n" +
                "        os.rename(old_filename, new_filename)\n" +
                "    end\n" +
                "end";
    }
}
