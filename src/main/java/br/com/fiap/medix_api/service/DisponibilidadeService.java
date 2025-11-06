package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.model.Colaborador;
import br.com.fiap.medix_api.model.Especialidade;
import br.com.fiap.medix_api.model.UnidadeSaude;
import br.com.fiap.medix_api.repository.ColaboradorRepository;
import br.com.fiap.medix_api.repository.EspecialidadeRepository;
import br.com.fiap.medix_api.repository.UnidadeSaudeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisponibilidadeService {

    private final EspecialidadeRepository especialidadeRepository;
    private final UnidadeSaudeRepository unidadeSaudeRepository;
    private final ColaboradorRepository colaboradorRepository;

    // Passo 1: Listar especialidades com profissionais ativos
    public List<Especialidade> listarEspecialidades() {
        return especialidadeRepository.findAllComMedicosAtivos();
    }

    // Passo 2: Listar unidades que atendem a especialidade
    public List<UnidadeSaude> listarUnidades(Long especialidadeId) {
        return unidadeSaudeRepository.findByEspecialidadeId(especialidadeId);
    }

    // Passo 3: Listar dias com possível atendimento
    public List<LocalDate> listarDiasDisponiveis(Long unidadeId, Long especialidadeId) {
        List<LocalDate> dias = new ArrayList<>();
        LocalDate hoje = LocalDate.now();
        // Exemplo simplificado: próximos 30 dias, apenas dias úteis
        for (int i = 1; i <= 30; i++) {
            LocalDate data = hoje.plusDays(i);
            if (data.getDayOfWeek() != DayOfWeek.SATURDAY && data.getDayOfWeek() != DayOfWeek.SUNDAY) {
                dias.add(data);
            }
        }
        return dias;
    }

    // Passo 4: Listar horários livres no dia escolhido
    public List<LocalTime> listarHorariosDisponiveis(Long unidadeId, Long especialidadeId, LocalDate data) {
        List<LocalTime> horariosLivres = new ArrayList<>();
        // Exemplo: expediente das 08h às 17h
        LocalTime inicioExpediente = LocalTime.of(8, 0);
        LocalTime fimExpediente = LocalTime.of(17, 0);

        for (LocalTime slot = inicioExpediente; slot.isBefore(fimExpediente); slot = slot.plusMinutes(30)) {
            LocalDateTime inicioSlot = LocalDateTime.of(data, slot);
            LocalDateTime fimSlot = inicioSlot.plusMinutes(30);

            // Verifica se existe pelo menos 1 médico livre neste slot na unidade/especialidade
            List<Colaborador> medicosLivres = colaboradorRepository.findDisponiveisPorHorario(unidadeId, especialidadeId, inicioSlot, fimSlot);
            if (!medicosLivres.isEmpty()) {
                horariosLivres.add(slot);
            }
        }
        return horariosLivres;
    }

    // Passo 5: Listar profissionais livres no horário escolhido
    public List<Colaborador> listarProfissionaisDisponiveis(Long unidadeId, Long especialidadeId, LocalDateTime dataHora) {
        LocalDateTime fim = dataHora.plusMinutes(30);
        return colaboradorRepository.findDisponiveisPorHorario(unidadeId, especialidadeId, dataHora, fim);
    }
}