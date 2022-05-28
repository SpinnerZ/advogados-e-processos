package online.publicacoes.avaliacaotecnica.repositories;

import online.publicacoes.avaliacaotecnica.entities.Process;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessRepository extends JpaRepository<Process, Long> {

  Boolean existsByNumber(Long number);

  Optional<Process> findByNumber(Long number);

  Long deleteByNumber(Long number);
}
