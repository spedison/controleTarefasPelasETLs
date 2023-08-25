source ./comum.sh
ARQUIVO_TEMP=$(/usr/bin/mktemp)

if [[ $DEBUG == 1 ]] 
then
  echo "Comum Executado.."
  echo "MKTEMP Rodado ..: " $ARQUIVO_TEMP
  echo "Rodando Java em : " $BASE_PROJECT
fi

$JAVA_HOME/bin/java -jar $BASE_PROJECT/controleTarefaScripts-0.0.1-SNAPSHOT.jar  testarConexaoBanco 2>&1 > $ARQUIVO_TEMP

if [[ $DEBUG == 1 ]]
then
  echo "JAVA Rodado ..."
  cat $ARQUIVO_TEMP
else
  if [[ $(ag "ERROR" $ARQUIVO_TEMP | ag "(Failed to initialize|Exception during pool)" | wc -l) -gt 1 ]]
  then
    echo "ERRO"
  else
    echo $(ag ControleTarefaScriptsApplication $ARQUIVO_TEMP | ag "(INFO|ERROR)" | ag ": ID\|.*$"  | cut -f2 -d"|")
  fi
fi

rm $ARQUIVO_TEMP
