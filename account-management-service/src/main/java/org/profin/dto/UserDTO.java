package org.profin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String password; // Здесь в реальной ситуации, возможно, лучше скрыть пароль
    // Возможно, дата создания и обновления не должны быть в DTO, но это зависит от требований
}
