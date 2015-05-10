package rabbeb.aws.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQS;

@Component
public class QueueValidator {
  final static Logger logger = LoggerFactory.getLogger(EmailSender.class);

  public void validateQueue(final AmazonSQS sqs, final String url) {
    logger.info("validating queue: {}", url);
    if (!sqs.listQueues().getQueueUrls().stream().peek(u -> logger.info(u))
        .anyMatch(u -> url.equalsIgnoreCase(url))) {
      throw new QueueException("Receiving SQS should be up!");
    }
  }
}
