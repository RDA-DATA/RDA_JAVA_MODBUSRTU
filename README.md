# 📡 삼성 SDK를 활용한 라즈베리파이 Modbus RTU 샘플코드


## 🚀 프로젝트 개요
이 프로젝트는 **삼성 SDK**를 활용하여 **라즈베리파이**에서 **Modbus RTU 통신**을 구현하는 방법을 설명합니다. RS485를 통해 라즈베리파이가 Modbus 마스터 또는 슬레이브로 동작할 수 있으며, 신뢰할 수 있는 산업용 통신을 가능하게 합니다.

## 🛠️ 주요 기능
- ✅ **Modbus RTU 마스터 및 슬레이브** 지원
- ✅ **UART (RX/TX) 또는 USB-to-RS485 어댑터를 통한 RS485 통신**
- ✅ **삼성 SDK 통합**을 통한 장치 제어 강화
- ✅ **라즈베리파이와 Modbus 호환 장치 간 실시간 데이터 전송**
- ✅ **Java 기반 구현**으로 플랫폼 간 호환성 제공

## 📌 요구 사항
### **하드웨어**
- 🖥️ **라즈베리파이 Zero 2 W / 3 / 4**
- 🔌 **RS485 to UART 어댑터** (또는 **RS485 to USB 어댑터**)
- 📡 **Modbus RTU 호환 장치 (예: PLC, 센서, Modbus 지원 아두이노)**

### **소프트웨어**
- 🏗️ **Java 17+**
- 📦 **삼성 SDK** ([다운로드](https://developer.samsung.com/))
- 🔄 **JLibModbus 라이브러리** ([GitHub](https://github.com/ksprojects/jlibmodbus))
- 🛠️ **라즈베리파이 OS (64비트 권장)**

## 🔧 설치 및 설정
### 1️⃣ **라즈베리파이에서 직렬 포트 활성화**
```sh
sudo raspi-config
# 인터페이스 옵션 > 직렬 포트로 이동
# 로그인 셸 비활성화 및 직렬 포트 활성화
```

### 2️⃣ **필수 패키지 설치**
```sh
sudo apt update && sudo apt install -y openjdk-17-jdk minicom
```

### 3️⃣ **레포지토리 클론 및 의존성 설치**
```sh
git clone https://github.com/your-repo/modbus-rtu-samsung-sdk.git
cd modbus-rtu-samsung-sdk
```

### 4️⃣ **Modbus RTU 애플리케이션 실행**
#### **마스터로 실행**
```sh
java -jar modbus-master.jar
```
#### **슬레이브로 실행**
```sh
java -jar modbus-slave.jar
```

## 📜 설정
`config.properties` 파일을 수정하여 직렬 포트 매개변수를 설정하세요:
```properties
port=/dev/serial0  # USB 어댑터 사용 시 /dev/ttyUSB0
baudrate=9600
parity=NONE
dataBits=8
stopBits=1
```

## 📡 테스트 및 디버깅
### **사용 가능한 직렬 포트 확인**
```sh
dmesg | grep tty
ls -l /dev/serial*
```

### **Modbus RTU 통신 모니터링**
```sh
sudo minicom -D /dev/serial0 -b 9600
```

## 📖 참고 자료
- 📘 **Modbus 프로토콜 사양**: [modbus.org](https://modbus.org)
- 📘 **삼성 SDK 문서**: [developer.samsung.com](https://developer.samsung.com/)

## ✨ 기여자
- **Your Name** - *개발자*

## 📜 라이선스
이 프로젝트는 **MIT 라이선스** 하에 배포됩니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

---
🔗 **GitHub 레포지토리**: [Your Repo Link](https://github.com/your-repo)

