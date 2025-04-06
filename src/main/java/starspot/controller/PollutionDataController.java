package starspot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PollutionDataController {

    @GetMapping("/api/pollution")
    public List<PollutionData> getPollutionData() {
        List<PollutionData> pollutionDataList = new ArrayList<>();
        BufferedReader reader = null;

        try {
            System.out.println("DEBUG: Starting to read pollution data...");

            // 加载 CSV 文件
            ClassPathResource resource = new ClassPathResource("Data/0303canada_light_pollution.csv");
            System.out.println("DEBUG: File exists: " + resource.exists());

            if (!resource.exists()) {
                System.err.println("DEBUG: File not found: Data/0303canada_light_pollution.csv");
                return pollutionDataList; // 返回空列表
            }

            reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));

            // 跳过 CSV 头部
            String header = reader.readLine();
            System.out.println("DEBUG: CSV Header: " + header);

            // 逐行读取 CSV 数据
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (lineCount % 100000 == 0) {
                    System.out.println("DEBUG: Processed " + lineCount + " lines...");
                }

                // 解析 CSV 行
                String[] parts = line.split(","); // 假设 CSV 使用逗号分隔
                if (parts.length == 3) {
                    double latitude = Double.parseDouble(parts[0].trim());
                    double longitude = Double.parseDouble(parts[1].trim());
                    double pollutionValue = Double.parseDouble(parts[2].trim());

                    // 添加到列表
                    pollutionDataList.add(new PollutionData(latitude, longitude,
                            pollutionValue));
                } else {
                    System.err.println("DEBUG: Skipping invalid line: " + line);
                }

                // 限制加载的数据量（测试时使用）
                if (lineCount >= 100000) {
                    break;
                }
            }

            System.out.println("DEBUG: Loaded " + pollutionDataList.size() + " pollution data points.");

        } catch (Exception e) {
            System.err.println("DEBUG: Error loading pollution data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (Exception e) {
                System.err.println("DEBUG: Error closing reader: " + e.getMessage());
            }
        }

        return pollutionDataList;
    }

    // 数据模型类
    static class PollutionData {
        private double latitude;
        private double longitude;
        private double pollutionValue;

        public PollutionData(double latitude, double longitude, double pollutionValue) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.pollutionValue = pollutionValue;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getPollutionValue() {
            return pollutionValue;
        }
    }
}