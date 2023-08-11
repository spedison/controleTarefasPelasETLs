source ./comum.sh
# echo "Inicio ou Fim = " $1
# echo "Número da Tarefa = " $2
# echo "Nome Atividade = " $3

atividade="IniciaAtividade"
if [[ $1 == "Fim" ]]
then
  atividade="TerminaAtividade"
fi

$JAVA_HOME/bin/java -jar $BASE_PROJECT/controleTarefaScripts-0.0.1-SNAPSHOT.jar  $atividade  $2 "$3" 2>&1 | grep ControleTarefaScriptsApplication | ag "(INFO|ERROR)" | ag [^P]ID | cut -f2 -d"|"