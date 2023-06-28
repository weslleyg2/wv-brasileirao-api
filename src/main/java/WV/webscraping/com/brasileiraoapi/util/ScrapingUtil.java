package wv.webscraping.com.brasileiraoapi.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import wv.webscraping.com.brasileiraoapi.dto.PartidaGoogleDTO;
@Service
public class ScrapingUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScrapingUtil.class);

	private static final String BASE_URL_GOOGLE = "https://www.google.com/search?q=";
	private static final String COMPLEMENTO_URL_GOOGLE = "&hl=pt-BR";
	
	private static final String DIV_PLACAR_EQUIPE_CASA = "div[class=imso_mh__l-tm-sc imso_mh__scr-it imso-light-font]";
	private static final String DIV_PLACAR_EQUIPE_VISITANTE = "div[class=imso_mh__r-tm-sc imso_mh__scr-it imso-light-font]";
	
	private static final String DIV_PARTIDA_ANDAMENTO = "div[class=imso_mh__lv-m-stts-cont]";
	private static final String DIV_PARTIDA_ENCERRADA = "span[class = imso_mh__ft-mtch imso-medium-font imso_mh__ft-mtchc]";
	
	
	private static final String DIV_DADOS_EQUIPE_CASA = "div[class=imso_mh__first-tn-ed imso_mh__tnal-cont imso-tnol]";
	private static final String DIV_DADOS_EQUIPE_VISITANTE = "div[class=imso_mh__second-tn-ed imso_mh__tnal-cont imso-tnol]";
	private static final String ITEM_LOGO = "img[class=imso_btl__mh-logo]";
	private static final String DIV_GOLS_EQUIPE_CASA = "div[class=imso_gs__tgs imso_gs__left-team]";
	private static final String DIV_GOLS_EQUIPE_VISITANTE = "div[class=imso_gs__tgs imso_gs__right-team]";
	private static final String ITEM_GOL = "div[class=imso_gs__gs-r]";
	private static final String DIV_PENALIDADES = "div[class=imso_mh_s__psn-sc]";
	
	private static final String CASA = "CASA";
	private static final String VISITANTE = "VISITANTE";
	private static final String HTTPS = "https:";
	private static final String SRC = "src";
	private static final String DATA_ORIGINAL_SRC = "data-original-src";
	private static final String SPAN = "span";
	private static final String PENALTIS = "Pênaltis";

	public PartidaGoogleDTO obtemInformacoesPartida(String url) {
		PartidaGoogleDTO partida = new PartidaGoogleDTO();

		Document document = null;

		try {
			LOGGER.info(url);
			// conecta no site
			document = Jsoup.connect(url).get();

			String title = document.title();
			LOGGER.info("Titulo da pagina {}", title);

			StatusPartida statusPartida = obtemStatusPartida(document);
			partida.setStatusPartida(statusPartida.toString());
			LOGGER.info("Status partida: {}", statusPartida);

			if (statusPartida != StatusPartida.PARTIDA_NAO_INICIADA) {
				String tempoPartida = obtemTempoPartida(document);
				partida.setTempoPartida(tempoPartida.toString());
				LOGGER.info("Tempo partida: {}", tempoPartida);
				
				Integer placarEquipeCasa = recuperaPlacarEquipe(document, DIV_PLACAR_EQUIPE_CASA);
				partida.setPlacarEquipeCasa(placarEquipeCasa);
				LOGGER.info("Placar Equipe Casa: {}", placarEquipeCasa);
				
				Integer placarEquipeVisitante = recuperaPlacarEquipe(document, DIV_PLACAR_EQUIPE_VISITANTE);
				partida.setPlacarEquipeVisitante(placarEquipeVisitante);
				LOGGER.info("Placar Equipe Visitante: {}", placarEquipeVisitante);
				
				String golsEquipeCasa = recuperaGolsEquipe(document,DIV_GOLS_EQUIPE_CASA);
				partida.setGolEquipeCasa(golsEquipeCasa);
				LOGGER.info("Gols Equipe Casa: {}", golsEquipeCasa);
	
				String golsEquipeVisitante = recuperaGolsEquipe(document,DIV_GOLS_EQUIPE_VISITANTE);
				partida.setGolEquipeVisitante(golsEquipeVisitante);
				LOGGER.info("Gols Equipe Visitante: {}", golsEquipeVisitante);
				
				Integer placarEstendidoEquipeCasa = buscaPenalidades(document, CASA);
				partida.setPlacarEstendidoEquipeCasa(placarEstendidoEquipeCasa.toString());
				LOGGER.info("Placar Estendido Equipe Casa: {}", placarEstendidoEquipeCasa);
				
				Integer placarEstendidoEquipeVisitante = buscaPenalidades(document, VISITANTE);
				partida.setPlacarEstendidoEquipeVisitante(placarEstendidoEquipeVisitante.toString());
				LOGGER.info("Placar Estendido Equipe Visitante: {}", placarEstendidoEquipeVisitante);
			}

			String nomeEquipeCasa = recuperaNomeEquipe(document,DIV_DADOS_EQUIPE_CASA);
			partida.setNomeEquipeCasa(nomeEquipeCasa);
			LOGGER.info("Nome Equipe Casa: {}", nomeEquipeCasa);

			String nomeEquipeVisitante = recuperaNomeEquipe(document, DIV_DADOS_EQUIPE_VISITANTE);
			partida.setNomeEquipeVisitante(nomeEquipeVisitante);
			LOGGER.info("Nome Equipe Visitante: {}", nomeEquipeVisitante);

			String urlLogoEquipeCasa = recuperaLogoEquipe(document, DIV_DADOS_EQUIPE_CASA);
			partida.setUrlLogoEquipeCasa(urlLogoEquipeCasa);
			LOGGER.info("Logo Equipe Casa: {}", urlLogoEquipeCasa);

			String urlLogoEquipeVisitante = recuperaLogoEquipe(document,DIV_DADOS_EQUIPE_VISITANTE);
			partida.setUrlLogoEquipeVisitante(urlLogoEquipeVisitante);
			LOGGER.info("Logo Equipe Visitante: {}", urlLogoEquipeVisitante);

			return partida;
		} catch (IOException e) {
			LOGGER.error("ERRO AO TENTAR CONECTAR NO GOOGLE COM JSOUP -> {}", e.getMessage());
		}

		return null;
	}

	public StatusPartida obtemStatusPartida(Document document) {
		// Situation
		// 1 - DEPARTURE_NOT_STARTED
		// 2 - DEPARTURE _IN_STARTED, / GAME_ROLLING / INTERVAL
		// 3 - MATCH_CLOSED,
		// 4 - DEPARTURE_PENALTIS;
		StatusPartida statusPartida = StatusPartida.PARTIDA_NAO_INICIADA;

		boolean isTempoPartida = document.select(DIV_PARTIDA_ANDAMENTO).isEmpty();
		if (!isTempoPartida) {
			String tempoPartida = document.select(DIV_PARTIDA_ANDAMENTO).first().text();
			statusPartida = StatusPartida.PARTIDA_EM_ANDAMENTO;

			if (tempoPartida.contains(PENALTIS)) {
				statusPartida = StatusPartida.PARTIDA_PENALTIS;
			}

			LOGGER.info(tempoPartida);
		}
		isTempoPartida = document.select(DIV_PARTIDA_ENCERRADA).isEmpty();
		if (!isTempoPartida) {
			statusPartida = StatusPartida.PARTIDA_ENCERRADA;
		}
		LOGGER.info(statusPartida.toString());
		return statusPartida;
	}

	public String obtemTempoPartida(Document document) {
		String tempoPartida = null;
		// Rolling game or break or penalties
		boolean isTempoPartida = document.select(DIV_PARTIDA_ANDAMENTO).isEmpty();
		if (!isTempoPartida) {
			tempoPartida = document.select(DIV_PARTIDA_ANDAMENTO).first().text();
		}
		isTempoPartida = document.select(DIV_PARTIDA_ENCERRADA).isEmpty();
		if (!isTempoPartida) {
			tempoPartida = document.select(DIV_PARTIDA_ENCERRADA).first()
					.text();
		}

		return corrigeTempoPartida(tempoPartida);
	}

	public String corrigeTempoPartida(String tempo) {
		if (tempo.contains("'")) {
			return tempo.replace(" ", "").replace("'", " min");
		} else {
			return tempo;
		}

	}

	public String recuperaNomeEquipe(Document document, String itemHtml) {
		Element elemento = document.selectFirst(itemHtml);
		String nomeEquipe = elemento.select(SPAN).text();

		return nomeEquipe;
	}

	public String recuperaLogoEquipe(Document document,String itemHtml) {
		Element elemento = document.selectFirst(DIV_DADOS_EQUIPE_CASA);
		Element imgElement = elemento.selectFirst(ITEM_LOGO);

		String urlLogo;
		if (imgElement.hasAttr(DATA_ORIGINAL_SRC)) {
			urlLogo = HTTPS + imgElement.attr(DATA_ORIGINAL_SRC);
		} else {
			urlLogo = HTTPS + imgElement.attr(SRC);
		}

		return urlLogo;
	}
	public Integer recuperaPlacarEquipe(Document document,String itemHtml){
		String placarEquipe = document.selectFirst(itemHtml).text();
		return formataPlacarStringInteger(placarEquipe);
	}
	
	public String recuperaGolsEquipe(Document document, String itemHtml) {
		List<String> golsEquipe = new ArrayList<>();
		
		Elements elementos = document.select(itemHtml).select(ITEM_GOL);
		for(Element e : elementos) {
			String infoGol = e.select(ITEM_GOL).text();
			golsEquipe.add(infoGol);
		}
		
		return String.join(", ", golsEquipe);
		
	}
	
	
	public Integer buscaPenalidades(Document document, String tipoEquipe) {
		boolean isPenalidades = document.select(DIV_PENALIDADES).isEmpty();
		
		if(!isPenalidades) {
			String penalidades = document.select(DIV_PENALIDADES).text();
			String penalidadesCompleta = penalidades.substring(0,5).replace(" ", "");
			String[] divisao = penalidadesCompleta.split("-");
			
			return tipoEquipe.equals(CASA) ? formataPlacarStringInteger(divisao[0]) : formataPlacarStringInteger(divisao[1]);
		}
		return null;
	}
	
	public Integer formataPlacarStringInteger(String placar) {
		Integer valor;
		try {
			valor = Integer.parseInt(placar);
		} catch (Exception e) {
			valor = 0;
		}
		return valor;
	}
	
	public String montaUrlGoogle(String nomeEquipeCasa, String nomeEquipeVisitante) {
		try {
			String equipeCasa = nomeEquipeCasa.replace(" ", "+").replace("-", "+");
			String equipeVisitante = nomeEquipeVisitante.replace(" ", "+").replace("-", "+");
			
			return BASE_URL_GOOGLE + equipeCasa + "+x+" + equipeVisitante + COMPLEMENTO_URL_GOOGLE;
		}catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		
		return null;
	}

}
