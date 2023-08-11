drop table ADMATENA.tb_etls_atividade cascade constraints
/

drop table ADMATENA.tb_etls_tarefa cascade constraints
/


create table ADMATENA.tb_etls_atividade (
    id number(19,0) generated as identity,
    nome varchar2(100 char),
    forcado number(1,0) check (forcado in (0,1)),
    inicio timestamp(6),
    fim timestamp(6),
    tarefa_id number(19,0),
    primary key (id))
/


create table ADMATENA.tb_etls_tarefa (
        id number(19,0) generated as identity,
        tipo varchar2(100 char),
        sucesso number(1,0) check (sucesso in (0,1)),
        contagem_registros_processos_elastic_final_processamento number(19,0),
        contagem_registros_processos_elastic_inicio_processamento number(19,0) not null,
        contagem_registros_variaveis_elastic_final_processamento number(19,0),
        contagem_registros_variaveis_elastic_inicio_processamento number(19,0) not null,
        inicio timestamp(6) not null,
        fim timestamp(6),
        primary key (id))
/

alter table ADMATENA.tb_etls_atividade add constraint FK_atividade_tarefa foreign key (tarefa_id) references ADMATENA.tb_etls_tarefa
/

commit
/