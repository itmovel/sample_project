import org.yaml.snakeyaml.Yaml

// O plugin GMavenPlus injeta a variável 'project' e 'log' automaticamente.

// Define os arquivos de entrada e saída usando as propriedades do Maven
def inputFile = new File(project.properties['openapi.spec.external'])
def outputFile = new File(project.properties['openapi.spec.internal'])

// Garante que o diretório de saída exista
outputFile.getParentFile().mkdirs()

log.info("Lendo a especificação OpenAPI de: " + inputFile)

// Carrega o YAML
def yaml = new Yaml()
def spec = yaml.load(inputFile.newDataInputStream())

// 1. Remove o parâmetro dos endpoints
spec.paths.each { path, methods ->
    methods.each { method, details ->
        if (details.parameters) {
            details.parameters.removeAll { param ->
                // Verifica se a chave '$ref' existe antes de acessá-la
                param.containsKey('$ref') && param.'$ref' == '#/components/parameters/ApiKey'
            }
        }
    }
}

// 2. Remove a definição do parâmetro
spec.components.parameters.remove('ApiKey')

// 3. Remove o esquema de segurança
spec.components.securitySchemes.remove('ApiKeyAuth')

// Salva o novo arquivo YAML
outputFile.withWriter('UTF-8') { writer ->
    yaml.dump(spec, writer)
}

log.info("Especificação OpenAPI filtrada e salva em: " + outputFile)