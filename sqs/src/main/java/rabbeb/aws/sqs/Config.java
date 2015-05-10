package rabbeb.aws.sqs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

@Configuration
@ComponentScan(basePackages = "rabbeb.aws.sqs")
public class Config {

  /*
   * The ProfileCredentialsProvider will return your [default] credential profile by reading from
   * the credentials file located at (~/.aws/credentials).
   */
  @Bean
  public AWSCredentials getAWSCredentials() {
    AWSCredentials credentials = null;
    try {
      credentials = new ProfileCredentialsProvider().getCredentials();
    } catch (Exception e) {
      throw new AmazonClientException(
          "Cannot load the credentials from the credential profiles file. "
              + "Please make sure that your credentials file is at the correct "
              + "location (~/.aws/credentials), and is in valid format.", e);
    }
    return credentials;
  }

  @Bean
  public Region getRegion() {
    Region region = Region.getRegion(Regions.EU_CENTRAL_1);
    return region;
  }

  @Bean
  public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}
