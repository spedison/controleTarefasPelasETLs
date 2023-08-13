export JAVA_HOME=/home/spedison/.sdkman/candidates/java/17.0.7-zulu/
export BASE_PROJECT=/mnt/dados/git/controleTarefaScripts/build/libs
#Perfis usandos na aplicacao
export spring_profiles_active=oracle,elastic-prod
#Dados do Elastic
export ELASTIC_USER=elastic
export ELASTIC_PASS=123456
export ELASTIC_HOST=elasticdb-7.casa.com.br
#Dados do Oracle
export BANCO=xe
export BANCO_HOST=oraclexe-w11.casa.com.br
export BANCO_USER=ADMATENA
export BANCO_PASS=112233

## Para habilitar o DEBUG basta colocar DEBUG=1
export DEBUG=

[ -z "$NOME_TAREFA" ] && export NOME_TAREFA="Etl Diaria Atena"