package com.example.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "http://localhost:5173")  // 允许来自 http://localhost:5173 的跨域请求
public class WeatherController {

    private static final String API_KEY = "0845a2cee3544d3e91d73f1b33ce1065";  // OpenWeatherMap 的 API 密钥
    private static final Logger log = LoggerFactory.getLogger(WeatherController.class);  // 创建 Logger 实例

    @GetMapping("/{city}")
    public ResponseEntity<?> getWeatherByCity(@PathVariable String city) {
        // 将城市名称从中文转换为拼音
        String pinyinCity = convertToPinyin(city);
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", pinyinCity, API_KEY);

        // 使用 Logger 记录信息，代替 System.out.println
        log.info("请求的城市: {}", city);
        log.info("请求的URL: {}", url);

        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(url, String.class);
            log.info("获取到的天气信息: {}", response);  // 输出 API 返回的数据
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("获取天气信息时出错: ", e);
            return ResponseEntity.status(500).body("无法获取天气信息");
        }
    }
    // 将中文城市名转换为拼音
    private String convertToPinyin(String chinese) {
        StringBuilder pinyinBuilder = new StringBuilder();
        for (char character : chinese.toCharArray()) {
            // 获取拼音数组，首个拼音是常用拼音
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(character);
            if (pinyinArray != null) {
                pinyinBuilder.append(pinyinArray[0].replaceAll("\\d", ""));  // 去掉声调
            } else {
                pinyinBuilder.append(character);  // 如果字符不是汉字，原样添加
            }
        }
        return pinyinBuilder.toString().toLowerCase();  // 转为小写
    }
}
