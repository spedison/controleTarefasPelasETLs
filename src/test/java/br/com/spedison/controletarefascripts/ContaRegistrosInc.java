package br.com.spedison.controletarefascripts;

import br.com.spedison.controletarefascripts.servico.ContaRegistros;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@Profile("elastic-null")
public class ContaRegistrosInc implements ContaRegistros {

    Long contagem = 15L;

    @Override
    public Long contagemVariaveis() {
        contagem += 100L;
        return contagem;
    }

    @Override
    public Long contagemProcessos() {
        contagem += 155L;
        return contagem;
    }

}
