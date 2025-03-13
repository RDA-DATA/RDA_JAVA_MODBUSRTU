package main.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] argv) {

        int poolSize = 1; // Thread를 여러개 사용할 경우 mqtt전송 오류 증상 발생하여 1로 설정 >> 필요 시, 변경하여 사용
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(poolSize);

        Runnable task = () -> {
            // Modbus 데이터 수집
            ModbusManager modbus = ModbusManager.getInstance();
            ModbusVO modbusVO = new ModbusVO();

            try {
                modbusVO = modbus.getModbusData();

                // IoT 연결
                MqttManager iot = MqttManager.getInstance();

                // mqtt 수신 데이터 확인(수신 테스트용)
                modbusVO.setReceiveMqttData(iot.getReceiveMqttData());

                // ModbusVO 데이터를 바탕화면에 텍스트 파일로 저장(테스트용)
                // saveDataToDesktop(modbusVO.toJsonString());

                // IoT로 데이터 전송
                iot.sendData(modbusVO.toJsonString());
                System.out.println("Send data: " + modbusVO.toJsonString());
                System.out.println("");

            } catch (Exception e) {
                System.out.println("Main Task Error: " + e.getMessage());
                // ModbusVO 데이터를 바탕화면에 텍스트 파일로 저장
                saveDataToDesktop(modbusVO.toJsonString());
            }
        };

        scheduler.scheduleAtFixedRate(task, 0, 60, TimeUnit.SECONDS);

        //////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////
        // 매일 전송에 실패한 데이터를 송신
        // 매일 00시마다 동작하는 스케줄러 추가
        Runnable dailyTask = () -> {
            // 여기에 매일 00시에 실행할 작업을 추가합니다.
            // TODO. 전송 실패 파일을 읽어서 재전송
            try {
                ModbusVO modbusVO = new ModbusVO();
                modbusVO.setGas1(9999);
                modbusVO.setGas2(9999);
                modbusVO.setHeatIndex1(9999.0);
                modbusVO.setHeatIndex2(9999.0);
                modbusVO.setHumi1(9999.0);
                modbusVO.setHumi2(9999.0);
                modbusVO.setPhoto1(9999);
                modbusVO.setPhoto2(9999);
                modbusVO.setRaspiData("9999-99-99");
                modbusVO.setRaspiTime("99:99:99");
                modbusVO.setReceiveMqttData("dailyTask Test");

                // IoT 연결
                MqttManager iot = MqttManager.getInstance();

                // IoT로 데이터 전송
                iot.sendData(modbusVO.toJsonString());
                System.out.println("Send data: " + modbusVO.toJsonString());

            } catch (Exception e) {
                System.out.println("DailyTask Error: " + e.getMessage());
            }
        };

        long initialDelay = computeInitialDelay();
        ScheduledExecutorService dailyScheduler = Executors.newSingleThreadScheduledExecutor();
        dailyScheduler.scheduleAtFixedRate(dailyTask, initialDelay, 24 * 60 * 60, TimeUnit.SECONDS);
    }

    /**
     * 현재 시간으로부터 다음 00시까지의 초기 지연 시간을 계산합니다.
     * 
     * @return 초기 지연 시간(초)
     */
    private static long computeInitialDelay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextMidnight = now.toLocalDate().atStartOfDay().plusDays(1);
        return java.time.Duration.between(now, nextMidnight).getSeconds();
    }

    /**
     * 오류 발생 시 데이터를 데스크탑의 LOG 폴더에 저장합니다.
     * 
     * @param data
     */
    private static void saveDataToDesktop(String data) {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String nowDate = now.format(dateFormatter);
        String fileName = "modbus_data_" + nowDate + ".txt";

        String userHome = System.getProperty("user.home");
        Path logFolderPath = Paths.get(userHome, "Desktop", "LOG");
        Path desktopPath = logFolderPath.resolve(fileName);

        try {
            if (!Files.exists(logFolderPath)) {
                Files.createDirectories(logFolderPath);
            }
            Files.write(desktopPath, data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Data saved to: " + desktopPath.toString());
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }
}