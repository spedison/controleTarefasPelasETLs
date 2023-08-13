source ./comum.sh

if [[ -n  "$1" ]]
then
  ID_TAREFA=$1
fi

if [[ $DEBUG == 1 ]]
then
  $JAVA_HOME/bin/java -jar $BASE_PROJECT/controleTarefaScripts-0.0.1-SNAPSHOT.jar  TerminaIDTarefa "$NOME_TAREFA" $ID_TAREFA
else
  $JAVA_HOME/bin/java -jar $BASE_PROJECT/controleTarefaScripts-0.0.1-SNAPSHOT.jar  TerminaIDTarefa "$NOME_TAREFA" $ID_TAREFA 2>&1  | ag ControleTarefaScriptsApplication | ag "(INFO|ERROR)" | ag [^P]ID | cut -f2 -d"|"
fi