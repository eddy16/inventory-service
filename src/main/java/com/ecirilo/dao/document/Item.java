package com.ecirilo.dao.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    private String id;
    private String code;
    private String stockId;
    private String name;
    private String description;
}
