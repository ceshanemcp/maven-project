/*
 * ===========================================================================
 *  Licensed Materials - Property of DeVry Education Group
 *
 * (C) Copyright DeVry Education Group 2015 All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are not permitted.
 *
 * ===========================================================================
 */
package ${package}.consumer.message;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

import oracle.jms.AQjmsConstants;
import oracle.jms.AQjmsFactory;
import oracle.jms.AQjmsSession;
import oracle.jms.AQjmsTopicReceiver;
import oracle.jms.TopicReceiver;

import org.apache.xmlbeans.XmlException;

public class HelperTopicConsumer {

    public static void main(String[] args) throws JMSException, XmlException {

        TopicConnectionFactory connectionFactory = AQjmsFactory.getTopicConnectionFactory(
                "jdbc:oracle:thin:@esbint2db1.int2.devry.edu:1521:BPLINT2", null);
        TopicConnection connection = connectionFactory.createTopicConnection("demosync", "Su0R0y1$");
        connection.start();

        TopicSession session = connection.createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);
        Topic topic = ((AQjmsSession) session).getTopic("demosync", "BNR_CRM_TEST_SCORE_TOPIC");

        TopicReceiver t_recv = ((AQjmsSession) session).createTopicReceiver(topic, "CRM_TEST_SCORE_SUBSCRIBER", null);
        Message jms_msg = t_recv.receiveNoWait();

        for (int i = 1; jms_msg != null; i++) {
            if (jms_msg instanceof TextMessage) {
                TextMessage t_msg = (TextMessage) jms_msg;
                String message = t_msg.getText().toString();
                System.out.println("\n\nPayload " + i + " :\n" + message);
                jms_msg.acknowledge();
                
                //TODO:  Add in the message parsing to get the object you are populating.
                //TestScoreObjectDocument tsObj = TestScoreObjectDocument.Factory.parse(message);
                //System.out.println("XML Object : " + tsObj.toString()+"\n\n");
                //System.out.println( i+". "+ tsObj.getTestScoreObject().getPayload().getTestScore().getOpportunityId());
            }

            if (i > 0) {
                break;
            }

            ((AQjmsTopicReceiver) t_recv).setNavigationMode(AQjmsConstants.NAVIGATION_NEXT_MESSAGE);
            jms_msg = t_recv.receiveNoWait();
        }

        connection.close();
    }
}