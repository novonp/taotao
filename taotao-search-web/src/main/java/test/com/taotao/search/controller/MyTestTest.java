package test.com.taotao.search.controller; 

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

public class MyTestTest {
    /**
     * ������
     * @throws JMSException
     */
    @Test
    public void show() throws JMSException {
        //1.����connectionFactory������Ҫָ��ip���˿�
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.89.107:61616");
        //2.����connectionFactory���󴴽�connection����
        Connection connection = connectionFactory.createConnection();
        //�������ӣ�ͨ��connection�����start����
        connection.start();
        //ͨ��connection���󴴽�session����
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //ʹ��session���󴴽�һ��destination����
        Queue queue = session.createQueue("test-queue");
        //ͨ��һ����Ե���Ϣ���󴴽�һ�������߶���
        MessageProducer producer = session.createProducer(queue);

        //����һ��massage����
            //����һ��textmessage����  �ַ�����Ϣ����
            TextMessage message = new ActiveMQTextMessage();
            //���õ�Ե���Ϣ
            message.setText("point to point");
        //ʹ��producer��������Ϣ
        producer.send(message);
        //�ر���Դ
        producer.close();
        session.close();
        connection.close();
    }
    /**
     * ������
     */
    @Test
    public void show1() throws JMSException, IOException {
        //����connectionFactory����
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.89.107:61616");
        //ͨ��connectionfactory���󴴽�connection����
        Connection connection = connectionFactory.createConnection();
        //��������
        connection.start();
        //ʹ��connection���󴴽�session����
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //ͨ��session���󴴽�һ��destitation����
        Queue queue = session.createQueue("test-queue");
        //ʹ��session���󴴽�consumer����
        MessageConsumer consumer = session.createConsumer(queue);
        //������Ϣ
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {

                    TextMessage textMessage = (TextMessage) message;
                    String text = null;
                    //��Ϣ����
                    System.out.println(text);

            }
        });
        //�ȴ���������
        System.in.read();
        //�ر���Դ
        consumer.close();
        session.close();
        connection.close();
    }

} 
