package rabbeb.aws.sqs;

import java.util.List;

import lombok.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

@Component
public class EmailReceiver {
  final static Logger logger = LoggerFactory.getLogger(EmailSender.class);
  private final AmazonSQS sqs;
  private final String queueUrl;
  private final String deadLetterUrl;

  @Autowired
  public EmailReceiver(@NonNull final AWSCredentials credentials, @NonNull final Region region,
      @Value("${emailQueueUrl}") final String emailQueueUrl,
      @Value("${deadletterQueueUrl}") final String deadLetterQueueUrl,
      final QueueValidator queueValidator) {
    sqs = new AmazonSQSClient(credentials);
    sqs.setRegion(region);
    this.queueUrl = emailQueueUrl;
    this.deadLetterUrl = deadLetterQueueUrl;
    queueValidator.validateQueue(sqs, queueUrl);
    queueValidator.validateQueue(sqs, deadLetterUrl);
  }

  public void handleDeadLetterQueue() {
    ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(deadLetterUrl);
    List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
    while (messages.size() > 0) {
      messages
          .stream()
          .peek(m -> logger.info("Received message: {}", m.getBody()))
          .forEach(m -> sqs.deleteMessage(new DeleteMessageRequest(queueUrl, m.getReceiptHandle())));
      messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
    }
  }

  public void receiveAndDrop() {
    ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
    List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
    logger.info("Fetched {} messages", messages.size());
    messages.stream().peek(m -> logger.info("Received message: {}", m.getBody()))
        .forEach(m -> sqs.deleteMessage(new DeleteMessageRequest(queueUrl, m.getReceiptHandle())));
  }
}
