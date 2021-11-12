@Library(['cicd-pipeline']) _

appName = 'six-letter-words-api'
image = "artifactory.mycompany.com:9002/myteam/${appName}"
version = ''

node {
    checkout scm
    def gradleProps = readProperties file: "gradle.properties"
    version = defineVersion(gradleProps.version)
}

Map envConfig(localEnv, companyEnvironment, deployFlow) {
    [
            deployFlow   : deployFlow,
            deployCommand: [
                    image   : 'docker.com/docker-kube-tools:v1.17.12-helm',
                    cmd     : """
                        export KUBECONFIG=\$(mktemp \$WORKSPACE/kube-config-XXXXXX)
                        echo "\$KUBECONFIG_YAML" > \$KUBECONFIG
                        helm upgrade -n default --install ${appName} charts/${appName} -f charts/${appName}/ci/${localEnv}.yaml --set appVersion=${version} --set dbPassword="\$DB_PASSWORD"
                """,
                    cerberus: [
                            sdbPath: "shared/myteam/${localEnv}",
                            sdbKeys: ['kubeconfig-myteam-cluster': 'KUBECONFIG_YAML', 'six-letter-words-rds': 'DB_PASSWORD']
                    ]
            ],
    ]
}

def config = [
        customBuildParameters: [
                string(name: 'VERSION', defaultValue: '', description: 'version description')
        ],

        buildFlow            : [
                PULL_REQUEST: ['Build', 'Compile', 'Scan', 'Sonar', 'Quality Gate'],
                DEVELOPMENT : ['Build', 'Compile', 'Scan', 'Sonar', 'Quality Gate'],
                RELEASE     : ['Build', 'Compile', 'Scan', 'Sonar', 'Quality Gate']
        ],

        branchMatcher        : [
                RELEASE    : ['master'],
                DEVELOPMENT: ['^(?!master).*$']
        ],

        build                : [
                serial: true,
                gradle: [
                        image: 'adoptopenjdk/openjdk11:alpine-slim',
                        cmd  : './gradlew clean build -PartifactoryUser=${ARTIFACTORY_USER} -PartifactoryAPIKey=${ARTIFACTORY_PASSWORD}',
                        cerberus: [
                                sdbPath: "shared/myteam/jenkins",
                                sdbKeys: ['username': 'ARTIFACTORY_USER', 'password': 'ARTIFACTORY_PASSWORD']
                        ]
                ],
                docker: [
                        cmd     : """
                            echo "\$ARTIFACTORY_PASSWORD" | docker login artifactory.mycompany.com:9002 -u "\$ARTIFACTORY_USER" --password-stdin
                            docker build -t ${image}:${version} -t ${image}:latest .
                            docker push ${image}:${version}
                            rm -rf .docker/config.json
                        """,
                        cerberus: [
                                sdbPath: "shared/myteam/jenkins",
                                sdbKeys: ['username': 'ARTIFACTORY_USER', 'password': 'ARTIFACTORY_PASSWORD']
                        ]
                ]
        ],

        sonar                : [
                image   : 'adoptopenjdk/openjdk11:alpine-slim',
                cmd     : 'export SONAR_USER_HOME=/tmp && ./gradlew sonarqube  -PartifactoryUser=${ARTIFACTORY_USER} -PartifactoryAPIKey=${ARTIFACTORY_PASSWORD} -Dsonar.login=${SONAR_TOKEN}',
                cerberus: [
                        sdbPath: 'shared/myteam/jenkins',
                        sdbKeys: ['username': 'ARTIFACTORY_USER', 'password': 'ARTIFACTORY_PASSWORD', 'sonar_token': 'SONAR_TOKEN']
                ]
        ],

        deploymentEnvironment: [
                'ST': envConfig('st', [
                        DEVELOPMENT: ['Deploy'],
                        RELEASE    : ['Deploy'],
                        DEPLOY_ONLY: ['Deploy']
                ]),
                'UA': envConfig('ua', [
                        DEPLOY_ONLY: ['Deploy']
                ]),
                'PR': envConfig('pr', [
                        DEPLOY_ONLY: ['Deploy']
                ])
        ],

        tags                 : [
                'application': appName,
        ],

        pra                  : [
                sdbPath    : "shared/myteam/jenkins",
                userNameKey: "username",
                passwordKey: "password",
        ],
]

def defineVersion(baseVersion) {
    if (!params.CUSTOM_VERSION?.trim()) {
        def branchName = (env.BRANCH_NAME ?: 'local').replaceAll("/", "_")
        if ('master' == branchName) {
            return "${baseVersion}-${env.BUILD_NUMBER ?: 0}"
        } else {
            return "${baseVersion}-${branchName}-${env.BUILD_NUMBER ?: 0}"
        }
    }
    return params.CUSTOM_VERSION
}

genericDeployPipeline(config)
