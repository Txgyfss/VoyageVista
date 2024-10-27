package com.example.backend.services;

import com.example.backend.models.Guide;
import com.example.backend.models.GuideTag;
import com.example.backend.models.GuideTagMap;
import com.example.backend.repositories.GuideRepository;
import com.example.backend.repositories.GuideTagMapRepository;
import com.example.backend.repositories.GuideTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.dto.GuideDTO;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class GuideService {

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private GuideTagRepository guideTagRepository;

    @Autowired
    private GuideTagMapRepository guideTagMapRepository;

    @Transactional
    public Guide updateGuide(Long id, GuideDTO guideDTO) {
        Guide existingGuide = guideRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "攻略不存在: " + id));

        // 更新攻略信息
        existingGuide.setTitle(guideDTO.getTitle());
        existingGuide.setContent(guideDTO.getContent());
        existingGuide.setExpensePerPerson(guideDTO.getExpensePerPerson());
        existingGuide.setDays(guideDTO.getDays());
        existingGuide.setIsPublic(guideDTO.getIsPublic());
        existingGuide.setUpdatedAt(LocalDateTime.now());

        guideRepository.save(existingGuide);

        // 删除旧标签
        guideTagMapRepository.deleteByGuide_GuideId(id);

        // 重新保存新标签
        for (Long tagId : guideDTO.getTagIds()) {
            GuideTag tag = guideTagRepository.findById(tagId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "标签ID不存在: " + tagId));
            GuideTagMap tagMap = new GuideTagMap();
            tagMap.setGuide(existingGuide);
            tagMap.setTag(tag);
            guideTagMapRepository.save(tagMap);
        }

        return existingGuide;
    }
    @Transactional
    public void deleteGuide(Long guideId) {
        guideRepository.deleteById(guideId);
    }
}
