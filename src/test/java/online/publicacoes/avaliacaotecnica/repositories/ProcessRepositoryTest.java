package online.publicacoes.avaliacaotecnica.repositories;

import online.publicacoes.avaliacaotecnica.entities.Process;
import online.publicacoes.avaliacaotecnica.fixturies.ProcessFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ProcessRepositoryTest {

  @Autowired ProcessRepository repository;

  Process process;

  @BeforeEach
  void setUp() {

    process = ProcessFixture.getProcess();
  }

  @Test
  @DisplayName("Quando process existe e está ativo")
  void existsByNumeroShoudReturnTrueWhenNumeroDoProcessoAlreadyExistsAndIsAtivo() {

    repository.save(process);

    assertTrue(repository.existsByNumber(1L));
  }

  @Test
  @DisplayName("Quando process existe e está inativo")
  void existsByNumeroShoudReturnFalseWhenNumeroDoProcessoAlreadyExistsAndIsInativo() {

    process.setArchived(false);

    repository.save(process);

    assertFalse(repository.existsByNumber(1L));
  }

  @Test
  @DisplayName("Quando process não existe")
  void existsByNumeroShoudReturnFalseWhenNumeroDoProcessoDoesNotExists() {

    repository.save(process);

    assertFalse(repository.existsByNumber(2L));
  }
}
