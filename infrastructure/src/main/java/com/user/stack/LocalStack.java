package com.user.stack;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.rds.*;
import software.amazon.awscdk.services.route53.CfnHealthCheck;

public class LocalStack extends Stack {

    public LocalStack(final App scope, final String id, final StackProps props) {
        super(scope, id, props);

        DatabaseInstance userServiceDb = createDatabase("UserServiceDB", "user-service-db");
        DatabaseInstance authServiceDb = createDatabase("AuthServiceDB", "auth-service-db");

        CfnHealthCheck userDbHealthCheck = createDbHealthCheck(userServiceDb, "UserServiceDBHealthCheck");
        CfnHealthCheck authDbHealthCheck = createDbHealthCheck(authServiceDb, "AuthServiceDBHealthCheck");
    }

    private DatabaseInstance createDatabase(final String id, final String dbName) {
        return DatabaseInstance.Builder.create(this, id)
                .engine(DatabaseInstanceEngine.postgres(
                        PostgresInstanceEngineProps.builder()
                                .version(PostgresEngineVersion.VER_17_2)
                                .build()))
                .instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO))
                .allocatedStorage(20)
                .credentials(Credentials.fromGeneratedSecret("admin_user"))
                .databaseName(dbName)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
    }

    private CfnHealthCheck createDbHealthCheck(final DatabaseInstance db, final String id) {
        return CfnHealthCheck.Builder.create(this, id)
                .healthCheckConfig(CfnHealthCheck.HealthCheckConfigProperty.builder()
                        .type("TCP")
                        .port(Token.asNumber(db.getDbInstanceEndpointPort()))
                        .ipAddress(db.getDbInstanceEndpointAddress())
                        .requestInterval(30)
                        .failureThreshold(3)
                        .build())
                .build();
    }

    public static void main(final String[] args) {
        App app = new App(AppProps.builder()
                .outdir("./cdk.out")
                .build());

        StackProps props = StackProps.builder()
                .synthesizer(new BootstraplessSynthesizer())
                .build();

        new LocalStack(app, "localstack", props);
        app.synth();
        System.out.println("App synthesizing in progress");
    }
}
