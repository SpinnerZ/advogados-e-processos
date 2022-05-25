package online.publicacoes.avaliacaotecnica.repositories;

import online.publicacoes.avaliacaotecnica.entities.Processo;
import online.publicacoes.avaliacaotecnica.factories.ProcessoFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ProcessoRepositoryTest {

  @Autowired ProcessoRepository repository;

  Processo processo;

  @BeforeEach
  void setUp() {

    processo = ProcessoFactory.getProcesso();
  }

  @Test
  @DisplayName("Quando processo existe e está ativo")
  void existsByNumeroShoudReturnTrueWhenNumeroDoProcessoAlreadyExistsAndIsAtivo() {

    repository.save(processo);

    assertTrue(repository.existsByNumeroAndAtivoIsTrue(1));
  }

  @Test
  @DisplayName("Quando processo existe e está inativo")
  void existsByNumeroShoudReturnFalseWhenNumeroDoProcessoAlreadyExistsAndIsInativo() {

    processo.setAtivo(false);

    repository.save(processo);

    assertFalse(repository.existsByNumeroAndAtivoIsTrue(1));
  }

  @Test
  @DisplayName("Quando processo não existe")
  void existsByNumeroShoudReturnFalseWhenNumeroDoProcessoDoesNotExists() {

    repository.save(processo);

    assertFalse(repository.existsByNumeroAndAtivoIsTrue(2));
  }
}
