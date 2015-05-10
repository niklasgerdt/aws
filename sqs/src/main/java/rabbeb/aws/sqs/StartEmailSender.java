package rabbeb.aws.sqs;

import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class StartEmailSender {
  private static final int SECOND = 1000;
  final static Logger logger = LoggerFactory.getLogger(StartEmailSender.class);

  public static void main(String[] args) {
    LoggerUtil.startLog();
    logger.info("starting email sender");

    ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
    ((AbstractApplicationContext) ctx).registerShutdownHook();

    EmailSender es = ctx.getBean(EmailSender.class);
    IntStream.rangeClosed(1, 10).peek(i -> logger.info("sending message {}", i))
        .peek(i -> sleep(i)).forEach(i -> es.send(i));

    ((AbstractApplicationContext) ctx).close();
    LoggerUtil.endLog();
  }

  static private void sleep(int i) {
    try {
      Thread.sleep(SECOND);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
