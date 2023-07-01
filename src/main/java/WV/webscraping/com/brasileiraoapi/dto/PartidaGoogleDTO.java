package wv.webscraping.com.brasileiraoapi.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import wv.webscraping.com.brasileiraoapi.util.StatusPartida;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PartidaGoogleDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private StatusPartida statusPartida;
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
