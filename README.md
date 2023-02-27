# Gradle Enterprise Build Scan™ Notifier

Gradle Enterprise Build Scan Notifier is a standalone application that monitors Gradle Enterprise and sends a notification to configured destinations when specified criteria is met. It is designed to help development teams stay informed about build failures and take action quickly to fix them.

The application can be configured to monitor multiple Gradle Enterprise servers and send notifications to one or more destinations. Notification rules are fully customizable, allowing you to specify the conditions, message, and destination of the notification.

Please note that this project is in early development and all features and functionality are subject to change.

## Installation

#### 1. Clone the repository

```shell
git clone https://github.com/erichaagdev/gradle-enterprise-build-scan-notifier.git
```

#### 2. Open the cloned directory

```shell
cd gradle-enterprise-build-scan-notifier
```

#### 3. Build the application

```shell
./gradlew build
```

#### 4. Copy the agent to a new directory

```shell
cp build-scan-notifier-agent/build/libs/build-scan-notifier-agent-0.0.1.jar ~/build-scan-notifier-agent.jar
```

#### 5. Configure the agent

The agent must be configured before it can be run. All agent properties are described in the [Configuration](#Configuration) section below. This is the minimum required configuration to run the agent. The configuration should be placed in an `application.yaml` file in the same directory as the agent executable.

```yaml
gradle-enterprise:
  servers:
    my-company-ge:
      server-url: https://ge.mycompany.com
      access-key: abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz
slack:
  webhooks:
    my-company-slack:
      webhook-url: https://hooks.slack.com/services/ABCDEFG/HIJKLMNOP/ABCDEFGHIJKLMNOPQRSTUVWXYZ
```

### 6. Run the agent

```shell
java -jar ~/build-scan-notifier-agent.jar 
```

## Configuration

The agent is configured using an `application.yaml` configuration file that specifies a set of notification rules, Gradle Enterprise servers to listen to, and destinations to send notifications to. The configuration file must be placed in the same directory as the agent executable.

While the properties file format is supported, it is not recommended due to the complexity of the configuration.

### Gradle Enterprise

The `gradle-enterprise` configuration defines a set of Gradle Enterprise servers to listen to.

```yaml
gradle-enterprise:
  servers:
    my-company-ge:
      server-url: https://ge.mycompany.com
      access-key: abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz
    another-ge:
      server-url: https://ge.another.com
      access-key: zyxwvutsrqponmlkjihgfedcbazyxwvutsrqponmlkjihgfedcba
```

Each Gradle Enterprise server is identified by a name (in this example, `my-company-ge` and `another-ge`).

- `server-url`: The URL of the Gradle Enterprise server.
- `access-key`: The access key to use when authenticating with the Gradle Enterprise server.

### Slack

The `slack` configuration section defines a list of Slack webhook URLs to send notifications to.

```yaml
slack:
  webhooks:
    backend-team-alerts:
      webhook-url: https://hooks.slack.com/services/ABCDEFG/HIJKLMNOP/ABCDEFGHIJKLMNOPQRSTUVWXYZ
    mobile-team-alerts:
      webhook-url: https://hooks.slack.com/services/GFEDCBA/PONMLKJIH/ZYXWVUTSRQPONMLKJIHGFEDCBA
```

Each Slack webhook is identified by a name (in this example, `backend-team-alerts` and `mobile-team-alerts`).

- `webhook-url`: The URL of the Slack webhook to send notifications to.

### Notification Rules

The `notification` configuration section defines a set of notification rules that specify the conditions under which notifications should be sent and the destinations to send notifications to.

```yaml
notification:
  rules:
    backend-build-failed:
      message: "The `%{projectName}` build has failed in CI.\n\nBuild Scan: %{link}"
      destinations:
        - backend-team-alerts
      condition:
        outcome: failed
        projects:
          - order-service
          - shipping-service
          - payment-service
        tags: [ CI ]
    mobile-build-failed:
      message: ":rotating_light: The build is broken! But don't fear, Gradle Enterprise is here!: %{link}"
      destinations:
        - mobile-team-alerts
      condition:
        outcome: failed
        projects:
          - mobile-order-service
        tags: [ CI, main ]
```

Each notification rule is identified by a name (in this example, `backend-build-failed` and `mobile-build-failed`).

- `message`: The message to include in the notification. This can contain placeholders that are replaced with information from the Build Scan. The supported placeholders are:
  - `%{link}`: The URL of the Build Scan.
  - `%{projectName}`: The name of the project that was built.
- `destinations`: The names of Slack webhook URLs to send the notification to.
- `condition`: The condition under which the notification should be sent. The criteria resembles the options available on the Gradle Enterprise dashboard where possible. The supported criteria are:
  - `outcome`: The outcome of the build. Valid values are `succeeded`, `failed`, and `any`. The default is `failed`.
  - `projects`: The set of project names that the condition applies to. Project names are not case-sensitive. An empty set applies to all projects. The default is an empty set.
  - `tags`: The set of tags that the condition applies to. Tags are not case-sensitive. Tags may be prefixed with `not:` to apply the condition when the tag is not present. An empty set means no tags will be considered. The default is an empty set.

## License

Gradle Enterprise Build Scan Notifier is released under the [`MIT License`](LICENSE) and is not an official Gradle, Inc. product.
