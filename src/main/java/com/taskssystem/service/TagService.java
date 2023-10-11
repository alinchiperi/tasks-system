package com.taskssystem.service;

import com.taskssystem.dto.TagDto;
import com.taskssystem.model.Tag;
import com.taskssystem.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> tagsFrom(List<TagDto> tags) {

        List<TagDto> tagDtos = Optional.ofNullable(tags).orElse(Collections.emptyList());
        return tagDtos.stream().map(this::toTag).collect(Collectors.toList());
    }

    private Tag toTag(TagDto tagDto) {
        Tag tag;
        Optional<Tag> tagFromName = tagRepository.findByName(tagDto.getName());
        if (tagFromName.isPresent()) {
            tag = tagFromName.get();
        } else {
            tag = tagDto.toTag();
            tagRepository.save(tag);

        }
        return tag;
    }
    public Optional<Tag> getTagByName(String name){
        return tagRepository.findByName(name);
    }
}
