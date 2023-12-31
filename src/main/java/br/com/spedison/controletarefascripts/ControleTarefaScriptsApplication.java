package br.com.spedison.controletarefascripts;

import br.com.spedison.controletarefascripts.servico.AtividadeServico;
import br.com.spedison.controletarefascripts.servico.TarefaServico;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Objects;

@SpringBootApplication
@ComponentScan(basePackages = {"br.com.spedison.controletarefascripts",
        "br.com.spedison.controletarefascripts.servico",
        "br.com.spedison.controletarefascripts.vo",
        "br.com.spedison.controletarefascripts.repository"
})
@EnableJpaRepositories(basePackages = "br.com.spedison.controletarefascripts.repository")
@EnableTransactionManagement
@EntityScan(basePackages = "br.com.spedison.controletarefascripts.vo")
@Log4j2
public class ControleTarefaScriptsApplication {

    private static final String STR_ERRO = "ID|ERRO";

    public static void main(String[] args) {
        SpringApplication.run(ControleTarefaScriptsApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx, ConfigurableApplicationContext cctx) {
        return args -> {

            if (verificaParametroVazio(args)) return;

            mostraVariaveisAmbiente();

            TarefaServico ts = ctx.getBean(TarefaServico.class);
            AtividadeServico as = ctx.getBean(AtividadeServico.class);
            ControleAtividadeMain cam = ctx.getBean(ControleAtividadeMain.class);
            ControleConexaoMain cc = ctx.getBean(ControleConexaoMain.class);
            ControleTarefaMain ctm = ctx.getBean(ControleTarefaMain.class);

            Long id = null;

            if (args[0].toLowerCase().contains("tarefa")) {
                id = ctm.executa(ts, args);
            } else if (args[0].toLowerCase().contains("atividade")) {
                id = cam.executa(as, args);
            } else if(args[0].toLowerCase().contains("conexao")){
                id = cc.executa(ts,args);
            }

            mostraID(id);

            cctx.close();
        };
    }

    private static void mostraID(Long id) {
        if (Objects.isNull(id))
            log.error(STR_ERRO);
        else
            log.info("ID|" + id);
    }

    private static void mostraVariaveisAmbiente() {
        String debug = System.getenv("DEBUG");
        if (!Objects.isNull(debug) && debug.trim().equals("1")) {
            System.getenv().forEach((k, v) ->
                    log.info("Valor de variável de ambiente [%s] => [%s]".formatted(k, v))
            );
        }
    }

    private static boolean verificaParametroVazio(String[] args) {
        if (args.length == 0) {
            // Completar com as demais opções.
            System.out.println("""
                    Opções :: 
                    IniciaIDTarefa  <Nome Tipo Tarefa>
                    TerminaIDTarefa <Nome Tipo Tarefa> <ID Tarefa>
                                        
                    """);
            return true;
        }
        return false;
    }
}
