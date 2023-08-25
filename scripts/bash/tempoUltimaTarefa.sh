source ./comum.sh

# O primeiro parametro eh o nome da tarefa, opcional.

if [[ -n $1  ]]
then
  NOME_TAREFA=$1
fi

if [[ $DEBUG == 1 ]]
then
  $JAVA_HOME/bin/java -jar $BASE_PROJECT/controleTarefaScripts-0.0.1-SNAPSHOT.jar  tempoUltimaExecucaoTarefa "$NOME_TAREFA"
else
  $JAVA_HOME/bin/java -jar $BASE_PROJECT/controleTarefaScripts-0.0.1-SNAPSHOT.jar  tempoUltimaExecucaoTarefa "$NOME_TAREFA" 2>&1 | ag ControleTarefaScriptsApplication | ag "(INFO|ERROR)" | ag ": ID\|.*$"  | cut -f2 -d"|"
fi
