package com.bank.user_service.api.request;

import com.bank.user_service.infrastructure.enums.UserType;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChangeUserTypeRequest {
    @Hidden
    private Long id;
    private UserType userType;

    public void withId(Long id) {
        this.id = id;
    }
}
