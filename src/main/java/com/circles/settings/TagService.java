package com.circles.settings;

import com.circles.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
