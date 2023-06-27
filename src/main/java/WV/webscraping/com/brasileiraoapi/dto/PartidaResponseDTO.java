package wv.webscraping.com.brasileiraoapi.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wv.webscraping.com.brasileiraoapi.entites.Partida;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PartidaResponseDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<Partida> equipes;

}
