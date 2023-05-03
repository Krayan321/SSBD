import java.nio.file.Paths;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@Testcontainers
public class BaseTest {

    static final Network NETWORK = Network.newNetwork();

    static MountableFile warFile =
            MountableFile.forHostPath(Paths.get("target/ssbd01-0.0.1.war").toAbsolutePath());

    @Container
    static PostgreSQLContainer postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                    .withNetwork(NETWORK)
                    .withDatabaseName("ssbd01")
                    .waitingFor(Wait.defaultWaitStrategy())
                    .withExposedPorts(5432)
                    .withInitScript("init_accounts.sql")
                    .withUsername("ssbd01admin")
                    .withPassword("admin")
                    .withNetworkAliases("postgres");
    @Container
    static GenericContainer microContainer = new GenericContainer<>("payara/micro:6.2023.2-jdk17")
            .withExposedPorts(8080, 8181)
            .withLogConsumer(outputFrame -> System.out.println(outputFrame.getUtf8String()))
            .dependsOn(postgreSQLContainer)
            .withNetwork(NETWORK)

            .withCopyToContainer(warFile, "/opt/payara/deployments/app.war")
            .waitingFor(Wait.forLogMessage(
                    ".* Payara Micro .* ready in .*\\s", 1))

            .withCommand("--deploy /opt/payara/deployments/app.war --sslPort 8181 --autoBindHttp --autoBindSsl");


}
