package main.java;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sds.iot.sdk.DefaultMessageListener;
import com.sds.iot.sdk.IotClient;
import com.sds.iot.sdk.auth.ita.ItaDirectAuth;
import com.sds.iot.sdk.message.IotMessage;

/**
 * IoT 연결 클래스
 * 싱글톤 패턴으로 구현되어 있습니다.
 * 1. IotConnector.getInstance()로 인스턴스를 가져옵니다.
 * 2. sendData()로 데이터를 전송합니다.
 * 3. closeConnection()으로 연결을 종료합니다.
 * 4. activateThing()으로 사물을 활성화합니다.
 * 5. IotListener 클래스를 참고하여 메시지 리스너를 구현합니다.
 */
public class MqttManager extends DefaultMessageListener {

    // IoT 연결 설정
    public String siteId = "C000000006";
    public String thingNumber = "8836";
    public String authCode = "aee31ac46fdb563a";

    private static MqttManager mqttManager;
    private IotClient client;
    private String receiveMqttData;

    // private 생성자
    private MqttManager() {
        // authCode - ita 장비 인증정보 입니다. 등록한 사물의 사물정보 화면에서 정보를 입력해주세요.
        ItaDirectAuth auth = new ItaDirectAuth(authCode);
        client = new IotClient(auth, siteId, thingNumber, "biot_client.properties");
        client.setCustomMessageListener(this);

        // PoC에 등록된 사물에 연결합니다.
        client.connect();
    }

    /**
     * 생성된 인스턴스를 반환합니다.
     * 연결 상태를 확인하여 재연결 합니다.
     * 
     * @return
     */
    public static synchronized MqttManager getInstance() {
        if (mqttManager == null || mqttManager.client == null) {
            mqttManager = new MqttManager();
        }

        if (!mqttManager.client.isConnected()) {
            // mqttManager.client.disconnect();
            mqttManager.client.connect();
        }

        return mqttManager;
    }

    public String getReceiveMqttData() {
        return this.receiveMqttData;
    }

    /**
     * json 형식의 데이터를 전송합니다.
     * 
     * @param jsonString 전송할 데이터(json 형식)
     */
    public void sendData(String jsonString) {
        try {
            // System.out.println("Send data: " + jsonString);
            // 사물에 데이터를 전송합니다.
            client.sendAttributes("Basic-AttrGroup", jsonString);
        } catch (Exception e) {
            System.out.println("Send data error : " + e.getMessage());
            throw e;
        }
    }

    /**
     * 서버에서 전송한 제어신호를 처리합니다.
     */
    @Override
    public void processProvisionMessage(IotMessage msg) {

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(msg.getDataString()).getAsJsonObject();

        this.receiveMqttData = jsonObject.get("sendMqttData").getAsString();

        // TODO. 메시지 타입에 따라 분기 처리 필요
        if ("Q".equals(msg.getMsgType())) {
            System.out.println("Q: " + msg.getDataString());
            IotMessage resMsg = msg.createResponse();
            getClient().send(resMsg);

        } else if ("N".equals(msg.getMsgType())) {
            System.out.println("N: " + msg.getDataString());
        } else if ("A".equals(msg.getMsgType())) {
            System.out.println("A: " + msg.getDataString());
        } else {
            System.out.println("Unknown message type: " + msg.getMsgType());
        }
    }

}
