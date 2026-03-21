package com.fitbridge.util;

import com.fitbridge.dto.SavedTreinoDTO;
import com.fitbridge.dto.SavedTreinoExercicioDTO;
import com.fitbridge.model.Treino;
import com.fitbridge.model.TreinoExercicio;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe utilitária para converter entre Treino e SavedTreinoDTO.
 * Realiza a serialização/desserialização de dados do treino em JSON.
 */
public class TreinoConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converte um objeto Treino em um SavedTreinoDTO com todos os seus dados.
     * @param treino O treino a ser convertido
     * @return DTO com dados do treino prontos para serem salvos
     */
    public static SavedTreinoDTO treinoToSavedDTO(Treino treino) {
        if (treino == null) {
            return null;
        }

        Set<SavedTreinoExercicioDTO> itens = new HashSet<>();
        if (treino.getItens() != null) {
            for (TreinoExercicio item : treino.getItens()) {
                SavedTreinoExercicioDTO exercicioDTO = new SavedTreinoExercicioDTO(
                    item.getId(),
                    item.getExercicio().getId(),
                    item.getExercicio().getNome(),
                    item.getExercicio().getDescricao(),
                    item.getExercicio().getMusculoAlvo(),
                    item.getExercicio().getMusculosAuxiliares(),
                    item.getExercicio().getDiretorioImagem(),
                    item.getSeries(),
                    item.getRepeticoes()
                );
                itens.add(exercicioDTO);
            }
        }

        return new SavedTreinoDTO(
            treino.getId(),
            treino.getTitulo(),
            treino.getGrupoMuscular(),
            treino.getDescricao(),
            itens
        );
    }

    /**
     * Serializa um SavedTreinoDTO em JSON string para armazenamento.
     * @param savedTreino O DTO do treino salvo
     * @return String JSON dos dados do treino
     */
    public static String serializeSavedTreino(SavedTreinoDTO savedTreino) {
        try {
            return objectMapper.writeValueAsString(savedTreino);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao serializar dados do treino", e);
        }
    }

    /**
     * Desserializa uma string JSON em um SavedTreinoDTO.
     * @param jsonData String JSON dos dados do treino
     * @return DTO com os dados desserializados
     */
    public static SavedTreinoDTO deserializeSavedTreino(String jsonData) {
        if (jsonData == null || jsonData.trim().isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonData, SavedTreinoDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao desserializar dados do treino", e);
        }
    }

    /**
     * Converte um Treino em string JSON e retorna a string serializada.
     * @param treino O treino a ser convertido
     * @return String JSON dos dados do treino
     */
    public static String treinoToJson(Treino treino) {
        SavedTreinoDTO savedTreino = treinoToSavedDTO(treino);
        return serializeSavedTreino(savedTreino);
    }
}
