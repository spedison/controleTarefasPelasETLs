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

            if (args.length == 0) {
                // Completar com as demais opções.
                System.out.println("""
                        Opções :: 
                        IniciaIDTarefa  <Nome Tipo Tarefa>
                        TerminaIDTarefa <Nome Tipo Tarefa> <ID Tarefa>
                        """);
                return;
            }

            TarefaServico ts = ctx.getBean(TarefaServico.class);
            AtividadeServico as = ctx.getBean(AtividadeServico.class);
            ControleAtividadeMain cam = ctx.getBean(ControleAtividadeMain.class);
            ControleTarefaMain ctm = ctx.getBean(ControleTarefaMain.class);

            Long id = null;

            if (args.length == 0) {
                log.error("Problemas com a quantidade de argumentos. (1)");
            } else {

                if (args[0].toLowerCase().contains("tarefa")) {
                    id = ctm.executa(ts, args);
                }

                if (args[0].toLowerCase().contains("atividade")) {
                    id = cam.executa(as, args);
                }
            }

            if (Objects.isNull(id))
                log.error(STR_ERRO);
            else
                log.info("ID|" + id);

            cctx.close();
        };
    }

}
