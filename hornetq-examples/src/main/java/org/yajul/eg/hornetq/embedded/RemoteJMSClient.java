package org.yajul.eg.hornetq.embedded;

import org.hornetq.jms.client.HornetQQueue;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * A remote JMS client.
 * <br>
 * User: josh
 * Date: 7/26/13
 * Time: 3:58 PM
 */
public class RemoteJMSClient
{
    private static final Logger log = Logger.getLogger(RemoteJMSClient.class.getName());

    public static void main(final String[] args)
    {
        try
        {
            // Step 2. Get the JMS Connection Factory
            ConnectionFactory cf = new RemoteConnectionFactoryProvider().getConnectionFactory();
            final HornetQQueue queue = new HornetQQueue("queue.exampleJmsQueue");
            runExample(cf,queue);
        }
        catch (JMSException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void runExample(ConnectionFactory cf, Queue queue) throws JMSException
    {
        Connection connection = null;
        Session session = null;
        try
        {
            // Step 4. Create the connection.
            connection = cf.createConnection();

            // Step 5. Create the session, and producer

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer = session.createProducer(queue);

            // Step 6. Create and send a message
            TextMessage message = session.createTextMessage();

            final String propName = "myprop";

            message.setStringProperty(propName, "Hello sent at " + new Date());

            log.info("Sending the message.");

            producer.send(message);

            // Step 7. Create the message consumer and start the connection
            MessageConsumer messageConsumer = session.createConsumer(queue);
            connection.start();

            // Step 8. Receive the message.
            Message messageReceived = messageConsumer.receive(1000);
            log.info("Received TextMessage:" + messageReceived.getStringProperty(propName));
        }
        catch (JMSException e)
        {
            e.printStackTrace();
        }
        finally
        {
            // Step 9. Be sure to close our resources!
            if (session != null)
                session.close();
            if (connection != null)
                connection.close();
        }
    }
}
