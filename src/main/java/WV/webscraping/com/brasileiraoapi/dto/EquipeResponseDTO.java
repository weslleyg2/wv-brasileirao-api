package wv.webscraping.com.brasileiraoapi.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wv.webscraping.com.brasileiraoapi.entites.Equipe;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipeResponseDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<Equipe> equipes;

}
