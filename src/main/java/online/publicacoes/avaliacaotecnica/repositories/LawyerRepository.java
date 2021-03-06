package online.publicacoes.avaliacaotecnica.repositories;

import online.publicacoes.avaliacaotecnica.entities.Lawyer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LawyerRepository extends JpaRepository<Lawyer, Long> {

  Boolean existsByUsername(String username);

  Optional<Lawyer> findByUsername(String username);

  Long deleteByUsername(String username);
}
