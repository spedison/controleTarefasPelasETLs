drop table admtarefas.atividade cascade constraints
/

drop table admtarefas.tarefa cascade constraints
/


create table admtarefas.atividade (
    id number(19,0) generated as identity,
    nome varchar2(100 char),
    forcado number(1,0) check (forcado in (0,1)),
    inicio timestamp(6),
    fim timestamp(6),
    tarefa_id number(19,0),
    primary key (id))
/


create table admtarefas.tarefa (
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

alter table admtarefas.atividade add constraint FK_atividade_tarefa foreign key (tarefa_id) references admtarefas.tarefa
/

commit
/



