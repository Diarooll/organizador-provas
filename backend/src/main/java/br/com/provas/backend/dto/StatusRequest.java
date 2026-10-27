package br.com.provas.backend.dto;

import br.com.provas.backend.model.StatusAvaliacao;
import jakarta.validation.constraints.NotNull;

public record StatusRequest(@NotNull StatusAvaliacao status) {
}
