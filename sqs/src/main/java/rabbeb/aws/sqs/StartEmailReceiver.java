package rabbeb.aws.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class StartEmailReceiver {
  final static Logger logger = LoggerFactory.getLogger(StartEmailReceiver.class);
  private static boolean kill = false;

  public static void main(String[] args) {
    setShutdownHook();
    LoggerUtil.startLog();
    logger.info("starting email receiver, kill process to stop receiving");

    ApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class);
    ((AbstractApplicationContext) ctx).registerShutdownHook();

    EmailReceiver er = ctx.getBean(EmailReceiver.class);
    er.handleDeadLetterQueue();
    while (!kill) {
      er.receiveAndDrop();
    }

    ((AbstractApplicationContext) ctx).close();
    LoggerUtil.endLog();
  }

  private static void setShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        logger.info("Shutdown hook ran!");
        kill = true;
      }
    });
  }
}
