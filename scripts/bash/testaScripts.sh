#!/usr/bin/bash
shopt -s expand_aliases
basePrj=/home/spedison/git/controleTarefaScripts/scripts
declare -i contaSucessos=0

echo "Executando Abertura Tarefa"
tarefa=$($basePrj/iniciaTarefa.sh)

echo "Abertura com ID ::: "$tarefa
if [[ $saida  !=  "ERRO" ]]
then
  echo "Sucesso"
  contaSucessos+=1
fi

atividades=("Limpa Banco" "Executa Carga" "Faz Processamento" "Fechamento" "Limpeza")
for numeroAtividade in "${!atividades[@]}"
do
  nomeAtividadeAtual=${atividades[$numeroAtividade]}
  echo "Processando Atividade = " $nomeAtividadeAtual " Tarefa = " $tarefa
  resultadoAtividade=$($basePrj/controlaAtividade.sh Inicio $tarefa "$nomeAtividadeAtual")
  if [[ ! -z "$resultadoAtividade" ]]
  then
     if [[ $resultadoAtividade != "ERRO"  ]]
      then
        contaSucessos+=1
      fi
  fi


  echo "Resultado da abertura da atividade :: ["  $resultadoAtividade "]"
  resultadoAtividade=$($basePrj/controlaAtividade.sh Fim $tarefa "$nomeAtividadeAtual")
  echo "Resultado da fechamento da atividade :: ["  $resultadoAtividade "]"
  if [[ ! -z "$resultadoAtividade" ]]
  then
     if [[ $resultadoAtividade != "ERRO"  ]]
      then
        contaSucessos+=1
      fi
  fi

done

echo "Segunda Abertura Tarefa"
tarefaSegunda=$($basePrj/iniciaTarefa.sh)

echo "Resultado Segunda Abertura ::: "$tarefaSegunda
if [[ -z $tarefaSegunda ]]
then
    echo "Saindo, pois não tive retorno, sistema de banco de dados deve estar fora."
    exit
fi

if [[ $tarefaSegunda  ==  "ERRO" ]]
then
  echo "Sucesso"
  contaSucessos+=1
fi

echo "Executando Fechamento Tarefa"
fechamentoTarefa=$($basePrj/terminaTarefa.sh $tarefa)
echo "Resultado ::: "$fechamento
if [[ $fechamentoTarefa  !=  "ERRO" ]]
then
  echo "Sucesso"
  contaSucessos+=1
fi

echo "Executando Segundo Fechamento"
segundoFechamentoTarefa=$($basePrj/terminaTarefa.sh $tarefa)
echo "Resultado ::: "$segundoFechamentoTarefa
if [[ $segundoFechamentoTarefa  ==  "ERRO" ]]
then
  echo "Sucesso"
  contaSucessos+=1
fi

echo "Fim dos testes deve ter 4 x 'Sucesso'"

echo "Quantidade de Sucessos = " $contaSucessos

if [[ $contaSucessos -eq 4 ]]
then
  echo "Todos os testes passaram."
else
  echo "Problemas pois nem todos os testes passaram"
fi