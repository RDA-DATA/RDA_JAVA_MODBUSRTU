package main.java;

import com.intelligt.modbus.jlibmodbus.data.ModbusHoldingRegisters;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.msg.request.ReadHoldingRegistersRequest;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadHoldingRegistersResponse;
import com.intelligt.modbus.jlibmodbus.serial.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ModbusManager {

    private static ModbusManager modbusManager;

    // private String serialPort = "/dev/serial0"; // 시리얼 포트
    private String serialPort = "/dev/ttyUSB0"; // USB 컨버터 포트
    // private String serialPort = "COM3"; // 컴퓨터 디버깅 포트

    private SerialPort.Parity seraialParity = SerialPort.Parity.NONE; // 패리티
    private int baudRate = 9600; // 보레이트
    private int dataBits = 8; // 데이터 비트
    private int stopBits = 1; // 스탑 비트

    private int registersStart = 0; // 읽을 시작 주소
    private int registerQuantity = 6; // 읽을 레지스터 개수
    private int modbusTimeOutMS = 20000; // 타임아웃 시간을 늘립니다.

    private ModbusMaster master; // Modbus 객체

    // private 생성자
    private ModbusManager() {
        try {
            // USB(Serial) to Modbus 설정
            SerialUtils.trySelectConnector();
            SerialUtils.setSerialPortFactory(new SerialPortFactoryJSerialComm());

            SerialParameters masterSerialParameters = new SerialParameters();
            masterSerialParameters.setDevice(serialPort);
            masterSerialParameters.setParity(seraialParity);
            masterSerialParameters.setBaudRate(new SerialPort.BaudRate(baudRate));
            masterSerialParameters.setDataBits(dataBits);
            masterSerialParameters.setStopBits(stopBits);

            // Modbus Master 객체생성
            master = ModbusMasterFactory.createModbusMasterRTU(masterSerialParameters);
            master.setResponseTimeout(modbusTimeOutMS);

        } catch (Exception e) {
            System.out.println("Modbus 설정 중 오류 발생");
        }
    }

    // 싱글톤 인스턴스를 반환
    public static synchronized ModbusManager getInstance() {
        if (modbusManager == null) {
            modbusManager = new ModbusManager();
        }
        return modbusManager;
    }

    /**
     * Modbus 데이터 요청
     * Slave 배열의 ID를 순차적으로 요청하여 데이터를 가져옵니다.
     * 
     * @return List<ModbusVO> Modbus 데이터 리스트
     */
    public ModbusVO getModbusData() {

        ModbusVO modbusVO = new ModbusVO();

        try {
            master.connect();
            while (master.isConnected() == false) {
                System.out.println("modbus 연결 중...");
                Thread.sleep(100);
            }

            // 현재 시간 추가
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String nowDate = now.format(dateFormatter);
            String nowTime = now.format(timeFormatter);
            modbusVO.setRaspiData(nowDate);
            modbusVO.setRaspiTime(nowTime);

            // Modbus - Slave 1 데이터 요청
            try {
                ReadHoldingRegistersResponse responseSlave1 = requestSlaveData(1);
                ModbusHoldingRegisters registers = responseSlave1.getHoldingRegisters();
                modbusVO.setSlaveId1(registers.get(0).intValue());
                modbusVO.setPhoto1(registers.get(1).intValue());
                modbusVO.setGas1(registers.get(2).intValue());
                modbusVO.setTemp1(registers.get(3).doubleValue());
                modbusVO.setHumi1(registers.get(4).doubleValue());
                modbusVO.setHeatIndex1(registers.get(5).doubleValue());
            } catch (Exception e) {
                System.out.println("Modbus - Slave 1 데이터 요청 중 오류 발생 : " + e.getMessage());
                master.disconnect();
                master.connect();
            }

            // Thread.sleep(1000); // 슬레이브 간의 지연 시간 추가

            // Modbus - Slave 2 데이터 요청
            try {
                ReadHoldingRegistersResponse responseSlave2 = requestSlaveData(2);
                ModbusHoldingRegisters registers2 = responseSlave2.getHoldingRegisters();
                modbusVO.setSlaveId2(registers2.get(0).intValue());
                modbusVO.setPhoto2(registers2.get(1).intValue());
                modbusVO.setGas2(registers2.get(2).intValue());
                modbusVO.setTemp2(registers2.get(3).doubleValue());
                modbusVO.setHumi2(registers2.get(4).doubleValue());
                modbusVO.setHeatIndex2(registers2.get(5).doubleValue());
            } catch (Exception e) {
                System.out.println("Modbus - Slave 2 데이터 요청 중 오류 발생 : " + e.getMessage());
                // master.disconnect();
                // master.connect();
            }

        } catch (Exception e) {
            // Modbus 통신 오류 발생 시 재시도
            System.out.println("Modbus 통신 중 오류 발생 : " + e.getMessage());
        } finally {
            try {
                master.disconnect();
            } catch (Exception e1) {
                System.out.println("Modbus 연결 종료 중 오류 발생 : " + e1.getMessage());
                master = null;
            }
        }

        return modbusVO;
    }

    /**
     * Modbus - Slave 데이터 요청
     */
    private ReadHoldingRegistersResponse requestSlaveData(int slaveId) throws Exception {

        ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest();
        request.setServerAddress(slaveId); // Slave ID
        request.setStartAddress(registersStart);
        request.setQuantity(registerQuantity);

        Thread.sleep(1000);
        master.processRequest(request);
        Thread.sleep(1000);

        return (ReadHoldingRegistersResponse) request.getResponse();
    }

}
