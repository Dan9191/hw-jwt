package com.example.hw_jwt.service.send;

import com.example.hw_jwt.configuration.AppSmppProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smpp.Connection;
import org.smpp.Session;
import org.smpp.TCPIPConnection;
import org.smpp.TimeoutException;
import org.smpp.WrongSessionStateException;
import org.smpp.pdu.BindResponse;
import org.smpp.pdu.BindTransmitter;
import org.smpp.pdu.PDUException;
import org.smpp.pdu.SubmitSM;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Сервис отправки сообщений по SMPP.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SmppNotificationService {

    private final AppSmppProperties appSmppProperties;


    private Session session;

    @PostConstruct
    public void init() throws Exception {
        connect();
    }

    public void connect() throws Exception {
        Connection connection = new TCPIPConnection(appSmppProperties.getHost(), appSmppProperties.getPort());
        session = new Session(connection);

        BindTransmitter bindRequest = new BindTransmitter();
        bindRequest.setSystemId(appSmppProperties.getSystemId());
        bindRequest.setPassword(appSmppProperties.getPassword());
        bindRequest.setSystemType(appSmppProperties.getSystemType());
        bindRequest.setInterfaceVersion((byte) 0x34);
        bindRequest.setAddressRange(appSmppProperties.getSourceAddr());

        BindResponse bindResponse = session.bind(bindRequest);
        if (bindResponse.getCommandStatus() != 0) {
            throw new IOException("Bind failed: " + bindResponse.getCommandStatus());
        }
    }

    public void sendSms(String phone, String message) throws Exception {
        log.info("sendSms phone={} message={}", phone, message);
        SubmitSM submitSM = new SubmitSM();
        submitSM.setSourceAddr(appSmppProperties.getSourceAddr());
        submitSM.setDestAddr(phone);
        submitSM.setShortMessage(message);
        session.submit(submitSM);
    }

    @PreDestroy
    public void disconnect() throws WrongSessionStateException, PDUException, IOException, TimeoutException {
        if (session != null) {
            session.unbind();
            session.getConnection().close();
        }
    }

}
