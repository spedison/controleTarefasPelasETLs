source ./comum.sh
#Deve receber Nada
#Nome padrão da ETL é EtlBasica
# TODO : Tornar esse parametros opcional
if [[ $DEBUG == 1 ]]
then
  $JAVA_HOME/bin/java -jar $BASE_PROJECT/controleTarefaScripts-0.0.1-SNAPSHOT.jar  IniciaIDTarefa EtlBasica 2>&1
else
  $JAVA_HOME/bin/java -jar $BASE_PROJECT/controleTarefaScripts-0.0.1-SNAPSHOT.jar  IniciaIDTarefa EtlBasica 2>&1 | ag ControleTarefaScriptsApplication | ag "(INFO|ERROR)" | ag [^P]ID | cut -f2 -d"|"
fi
