package rabbeb.aws.sqs;

import lombok.NonNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;

@Service
@PropertySource("classpath:/application.properties")
public class EmailSender {
  final static Logger logger = LoggerFactory.getLogger(EmailSender.class);
  private final AmazonSQS sqs;
  private final String queueUrl;

  @Autowired
  public EmailSender(@NonNull final AWSCredentials credentials, @NonNull final Region region,
      @Value("${emailQueueUrl}") final String emailQueueUrl, final QueueValidator queueValidator) {
    sqs = new AmazonSQSClient(credentials);
    sqs.setRegion(region);
    this.queueUrl = emailQueueUrl;
    queueValidator.validateQueue(sqs, queueUrl);
  }

  public void send(int i) {
    sqs.sendMessage(new SendMessageRequest().withQueueUrl(queueUrl).withMessageBody(
        "This is my message number " + i + "."));
  }
}
