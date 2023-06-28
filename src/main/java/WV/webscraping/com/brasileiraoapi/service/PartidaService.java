package wv.webscraping.com.brasileiraoapi.service;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wv.webscraping.com.brasileiraoapi.dto.PartidaDTO;
import wv.webscraping.com.brasileiraoapi.dto.PartidaGoogleDTO;
import wv.webscraping.com.brasileiraoapi.dto.PartidaResponseDTO;
import wv.webscraping.com.brasileiraoapi.entites.Partida;
import wv.webscraping.com.brasileiraoapi.exception.NotFoundException;
import wv.webscraping.com.brasileiraoapi.repository.PartidaRepository;

@Service
public class PartidaService {

	@Autowired
	private PartidaRepository partidaRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private EquipeService equipeService;
	
	public Partida buscarPartidaPorId(Long id) {
		return partidaRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Nenhuma partida foi encontrado com o id informado: " + id));
	}

	public PartidaResponseDTO listarPartidas() {
		PartidaResponseDTO partidas = new PartidaResponseDTO();
		partidas.setPartidas(partidaRepository.findAll());
		
		return partidas;
	}

	public Partida inserirPartida(PartidaDTO dto) {
		Partida partida = modelMapper.map(dto, Partida.class);
		partida.setEquipeCasa(equipeService.buscarEquipePorNome(dto.getNomeEquipeCasa()));
		partida.setEquipeVisitante(equipeService.buscarEquipePorNome(dto.getNomeEquipeVisitante()));
		
		return salvarPartida(partida);
	}
	
	private Partida salvarPartida(Partida partida) {
		return partidaRepository.save(partida);
	}

	public void alterarPartida(Long id, PartidaDTO dto) {
		boolean exists = partidaRepository.existsById(id);
		if(!exists) {
			throw new NotFoundException("Não foi possivel atualizar a partida: ID inexistente");
		}
		Partida partida = buscarPartidaPorId(id);
		partida.setEquipeCasa(equipeService.buscarEquipePorNome(dto.getNomeEquipeCasa()));
		partida.setEquipeVisitante(equipeService.buscarEquipePorNome(dto.getNomeEquipeVisitante()));
		partida.setDataHoraPartida(dto.getDataHoraPartida());
		partida.setLocalPartida(dto.getLocalPartida());
		
		salvarPartida(partida);
		
	}

	public void atualizaPartida(Partida partida, PartidaGoogleDTO partidaGoogle) {
		partida.setPlacarEquipeCasa(partidaGoogle.getPlacarEquipeCasa());
		partida.setPlacarEquipeVisitante(partidaGoogle.getPlacarEquipeVisitante());
		partida.setGolsEquipeCasa(partidaGoogle.getGolEquipeCasa());
		partida.setGolsEquipeVisitante(partidaGoogle.getGolEquipeVisitante());
		partida.setPlacarEstendidoEquipeCasa(Integer.parseInt(partidaGoogle.getPlacarEstendidoEquipeCasa()));
		partida.setPlacarEstendidoEquipeVisitante(Integer.parseInt(partidaGoogle.getPlacarEstendidoEquipeVisitante()));

		partida.setTempoPartida(partidaGoogle.getTempoPartida());
		
		salvarPartida(partida);
	}

	public List<Partida> listarPartidasPeriodo() {
		return partidaRepository.listarPartidasPeriodo();
	}

	public Integer buscarQuantidadePartidasPeriodo() {
		return partidaRepository.buscarQuantidadePartidasPeriodo();
	}
}
