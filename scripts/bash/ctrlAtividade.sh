source ./comum.sh
# echo "Inicio ou Fim = " $1
# echo "Número da Tarefa = " $2
# echo "Nome Atividade = " $3

atividade="IniciaAtividade"
if [[ $1 == "Fim" ]]
then
  atividade="TerminaAtividade"
fi

if [[ -z $ID_TAREFA ]]
then
  echo "Número da tarefa não definido"
  exit
fi

if [[ $DEBUG == 1 ]]
then
  $JAVA_HOME/bin/java -jar $BASE_PROJECT/controleTarefaScripts-0.0.1-SNAPSHOT.jar  $atividade  $ID_TAREFA "$2"
else
  $JAVA_HOME/bin/java -jar $BASE_PROJECT/controleTarefaScripts-0.0.1-SNAPSHOT.jar  $atividade  $ID_TAREFA "$2" 2>&1 | grep ControleTarefaScriptsApplication | ag "(INFO|ERROR)" | ag " : ID\|.*$" | cut -f2 -d"|"
fi