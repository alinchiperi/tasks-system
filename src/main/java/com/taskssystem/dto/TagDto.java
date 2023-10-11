package com.taskssystem.dto;

import com.taskssystem.model.Tag;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TagDto {
    private Integer id;
    private String name;

    public static TagDto from(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }

    public Tag toTag() {
        return new Tag(id, name);
    }
}
