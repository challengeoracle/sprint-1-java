package br.com.fiap.medix_api.service;

import br.com.fiap.medix_api.dto.response.*;
import br.com.fiap.medix_api.model.*;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    public RespostaAgendamentoDto mapAgendamentoToDto(Agendamento agendamento) {
        RespostaAgendamentoDto dto = new RespostaAgendamentoDto();
        dto.setId(agendamento.getId());
        dto.setDataHoraInicio(agendamento.getDataHoraInicio());
        dto.setDataHoraFim(agendamento.getDataHoraFim());
        dto.setTipo(agendamento.getTipo());
        dto.setStatus(agendamento.getStatus());
        dto.setObservacoes(agendamento.getObservacoes());
        dto.setIdPaciente(agendamento.getPaciente().getId());
        dto.setNomePaciente(agendamento.getPaciente().getNome());
        dto.setIdColaborador(agendamento.getColaborador().getId());
        dto.setNomeColaborador(agendamento.getColaborador().getNome());
        dto.setIdUnidadeSaude(agendamento.getUnidadeSaude().getId());
        dto.setNomeUnidadeSaude(agendamento.getUnidadeSaude().getNome());
        if (agendamento.getSala() != null) {
            dto.setIdSala(agendamento.getSala().getId());
            dto.setNomeSala(agendamento.getSala().getNome());
        }
        return dto;
    }

    public RespostaColaboradorDto mapColaboradorToDto(Colaborador c) {
        RespostaColaboradorDto dto = new RespostaColaboradorDto();
        dto.setId(c.getId());
        dto.setNome(c.getNome());
        dto.setEmail(c.getEmail());
        dto.setCpf(c.getCpf());
        dto.setTipoUsuario(c.getTipoUsuario());
        dto.setDescricaoCargo(c.getDescricaoCargo());
        dto.setNumeroRegistroProfissional(c.getNumeroRegistroProfissional());
        dto.setDataAdmissao(c.getDataAdmissao());

        if (c.getUnidadeSaude() != null) {
            dto.setUnidadeSaude(mapUnidadeToDto(c.getUnidadeSaude()));
        }
        if (c.getEspecialidade() != null) {
            dto.setEspecialidade(mapEspecialidadeToDto(c.getEspecialidade()));
        }
        return dto;
    }

    public RespostaPacienteDto mapPacienteToDto(Paciente paciente) {
        RespostaPacienteDto dto = new RespostaPacienteDto();
        dto.setId(paciente.getId());
        dto.setNome(paciente.getNome());
        dto.setEmail(paciente.getEmail());
        dto.setCpf(paciente.getCpf());
        dto.setTipoUsuario(paciente.getTipoUsuario());
        dto.setDataNascimento(paciente.getDataNascimento());
        dto.setTipoSanguineo(paciente.getTipoSanguineo());
        dto.setGenero(paciente.getGenero());
        dto.setAlergias(paciente.getAlergias());
        return dto;
    }

    public RespostaSalaDto mapSalaToDto(Sala sala) {
        RespostaSalaDto dto = new RespostaSalaDto();
        dto.setId(sala.getId());
        dto.setNome(sala.getNome());
        dto.setTipo(sala.getTipo());
        if (sala.getUnidadeSaude() != null) {
            dto.setIdUnidadeSaude(sala.getUnidadeSaude().getId());
            dto.setNomeUnidadeSaude(sala.getUnidadeSaude().getNome());
        }
        return dto;
    }

    public RespostaUnidadeSaudeDto mapUnidadeToDto(UnidadeSaude unidade) {
        RespostaUnidadeSaudeDto dto = new RespostaUnidadeSaudeDto();
        dto.setId(unidade.getId());
        dto.setNome(unidade.getNome());
        dto.setEndereco(unidade.getEndereco());
        dto.setTipoUnidade(unidade.getTipoUnidade());
        return dto;
    }

    public RespostaEspecialidadeDto mapEspecialidadeToDto(Especialidade especialidade) {
        RespostaEspecialidadeDto dto = new RespostaEspecialidadeDto();
        dto.setId(especialidade.getId());
        dto.setNome(especialidade.getNome());
        return dto;
    }
}