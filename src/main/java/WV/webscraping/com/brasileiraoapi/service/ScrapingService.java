package wv.webscraping.com.brasileiraoapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wv.webscraping.com.brasileiraoapi.dto.PartidaGoogleDTO;
import wv.webscraping.com.brasileiraoapi.entites.Partida;
import wv.webscraping.com.brasileiraoapi.util.ScrapingUtil;

@Service
public class ScrapingService {
	@Autowired
	private ScrapingUtil scrapingUtil;
	
	@Autowired
	private PartidaService partidaService;
	
	public void verificaPartidaPeriodo() {
		Integer quantidadePartida = partidaService.buscarQuantidadePartidasPeriodo();
		
		if (quantidadePartida > 0) {
			List<Partida> partidas = partidaService.listarPartidasPeriodo();
			
			partidas.forEach(partida -> {
				String urlPartida = scrapingUtil.montaUrlGoogle(
						partida.getEquipeCasa().getNomeEquipe(),
						partida.getEquipeVisitante().getNomeEquipe());
				
				PartidaGoogleDTO partidaGoogle = scrapingUtil.obtemInformacoesGoogle(urlPartida);
				
				partidaService.atualizaPartida(partida, partidaGoogle);
			});
		}
	}

}
