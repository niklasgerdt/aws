package rabbeb.aws.sqs;

@SuppressWarnings("serial")
public class QueueException extends RuntimeException {

  public QueueException(String msg) {
    super(msg);
  }
}
