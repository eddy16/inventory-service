package com.ecirilo.controller.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @Size(min = 3)
    @NotBlank
    private String code;
    @Size(min = 3)
    @NotBlank
    private String name;
    @Size(max = 35)
    @NotBlank
    private String description;
}
