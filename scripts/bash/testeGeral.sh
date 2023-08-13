export NOME_TAREFA="Etl Atena"
export ID_TAREFA=$(./iniciaTarefa.sh)

echo "Gerado o ID da tarefa :: "$ID_TAREFA

if [[ $ID_TAREFA == "ERRO" ]]
then
  echo "Problemas no processamento (1)"
  exit
fi

echo "Iniciciando as Atividades."

for  i in $(seq 40) ; do

  echo "Testando Atividades "$i
  NOME_ATIVIDADE="Atividade #$i"
  INI=$(./ctrlAtividade.sh Inicio "$NOME_ATIVIDADE")
  sleep 5
  FIM=$(./ctrlAtividade.sh Fim "$NOME_ATIVIDADE")

  echo "INICIO " $INI " - FIM " $FIM

done

FINAL=$(./terminaTarefa.sh)
echo "Terminou o processamento [ $ID_TAREFA ] - $NOME_TAREFA com Código $FINAL"
export NOME_TAREFA=
export ID_TAREFA=

