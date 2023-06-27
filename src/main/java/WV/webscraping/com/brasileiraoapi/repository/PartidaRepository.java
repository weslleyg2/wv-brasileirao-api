package wv.webscraping.com.brasileiraoapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import wv.webscraping.com.brasileiraoapi.entites.Partida;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {

}
