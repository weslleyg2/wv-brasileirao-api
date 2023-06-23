package WV.webscraping.com.brasileiraoapi.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class PartidaGoogleDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String statusPartida;
	private String tempoPartida;
	//home team information
	private String nomeEquipeCasa;
	private String urlLogoEquipeCasa;
	private Integer placarEquipeCasa;
	private String golEquipeCasa;
	private String  placarEstendidoEquipeCasa;
	//away team information
	private String nomeEquipeVisitante;
	private String urlLogoEquipeVisitante;
	private Integer placarEquipeVisitante;
	private String golEquipeVisitante;
	private String  placarEstendidoEquipeVisitante;
	
}
