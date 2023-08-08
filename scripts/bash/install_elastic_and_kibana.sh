docker network create elastic
docker pull elasticsearch:8.8.1
docker pull docker.elastic.co/kibana/kibana:8.9.0

docker run -d --name es-node01 --net elastic -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:8.8.1
-- Configurações de Segurança são exibidas na tela.

docker run --name kibana --net elastic -p 5601:5601 docker.elastic.co/kibana/kibana:8.8.1

-- Gerar novas senhas no Elastic.
docker exec -it es-node01 /usr/share/elasticsearch/bin/elasticsearch-reset-password -u elastic

docker exec -it es-node01 /usr/share/elasticsearch/bin/elasticsearch-create-enrollment-token --scope kibana

-- Intalação do Elastic Sem Senha nenhuma usando http --------------------------------------

docker run \
      --name elasticsearch \
      --net elastic \
      -p 9200:9200 \
      -e discovery.type=single-node \
      -e ES_JAVA_OPTS="-Xms4g -Xmx4g"\
      -e xpack.security.enabled=false \
      -it \
      docker.elastic.co/elasticsearch/elasticsearch:8.2.2
// Acessível com http e sem senha.
docker run --name kibana --net elastic -p 5601:5601 docker.elastic.co/kibana/kibana:8.2.2

-- -------------------------------------

docker rm -f elasticsearch kibana
docker rm -f kibana

docker run \
      --name elasticsearch \
      --net elastic \
      -p 9200:9200 \
      -p 9300:9300 \
      -e discovery.type=single-node \
      -e ES_JAVA_OPTS="-Xms4g -Xmx4g" \
      -e xpack.security.enabled=true \
      docker.elastic.co/elasticsearch/elasticsearch:8.2.2
// Acessível com http e sem senha.
docker run --name kibana --net elastic -p 5601:5601 docker.elastic.co/kibana/kibana:8.2.2


elastic = 123456
spedison = 12345678

