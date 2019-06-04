package test.com.taotao.search.controller; 

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;

public class MyTestTest {
    /**
     * 生产者
     * @throws JMSException
     */
    @Test
    public void show() throws JMSException {
        //1.创建connectionFactory对象，需要指定ip及端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.89.107:61616");
        //2.创建connectionFactory对象创建connection对象
        Connection connection = connectionFactory.createConnection();
        //开启连接，通过connection对象的start方法
        connection.start();
        //通过connection对象创建session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //使用session对象创建一个destination对象
        Queue queue = session.createQueue("test-queue");
        //通过一个点对点消息对象创建一个生产者对象
        MessageProducer producer = session.createProducer(queue);

        //创建一个massage对象
            //创建一个textmessage对象  字符串消息对象
            TextMessage message = new ActiveMQTextMessage();
            //设置点对点消息
            message.setText("point to point");
        //使用producer对象发送消息
        producer.send(message);
        //关闭资源
        producer.close();
        session.close();
        connection.close();
    }
    /**
     * 消费者
     */
    @Test
    public void show1() throws JMSException, IOException {
        //创建connectionFactory对象
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.89.107:61616");
        //通过connectionfactory对象创建connection对象
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //使用connection对象创建session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //通过session对象创建一个destitation对象
        Queue queue = session.createQueue("test-queue");
        //使用session对象创建consumer对象
        MessageConsumer consumer = session.createConsumer(queue);
        //接收消息
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {

                    TextMessage textMessage = (TextMessage) message;
                    String text = null;
                    //消息内容
                    System.out.println(text);

            }
        });
        //等待键盘输入
        System.in.read();
        //关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

} 
