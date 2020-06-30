package com.circles.settings;

import com.circles.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;


    public Tag addTag(Tag tag) {
        tagRepository.save(tag);
        return tag;
    }


    public Tag findByTitle(String tagName) {
        return tagRepository.findByTitle(tagName);
    }

    public void removeTag(Tag tag) {
       tagRepository.delete(tag);
    }

    public List<String> findAll() {
        return tagRepository.findAll().stream().map(Tag::getTitle).collect(Collectors.toList());
    }
}
