package br.com.spedison.controletarefascripts;

import br.com.spedison.controletarefascripts.servico.TarefaServico;
import br.com.spedison.controletarefascripts.vo.Tarefa;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
class ControleTarefaScriptsApplicationTests {

    @Autowired
    TarefaServico tarefaServico;

    @Test
    void contextLoads() {
        Assertions.assertTrue(true);
    }

    @Test
    void criaEFechaTarefa(){
        String tipoTarefa = "teste de teste";
        Tarefa t = tarefaServico.criaNovaTarefa(tipoTarefa);
        log.info("Criada a Tarefa : "+t.getId());
        Tarefa tFechado = tarefaServico.fechaTarefa(tipoTarefa,t.getId());
        Assertions.assertTrue(tFechado.getSucesso());
        Assertions.assertEquals(t.getId(), tFechado.getId());
        log.info("Teste terminado.");
    }

}
