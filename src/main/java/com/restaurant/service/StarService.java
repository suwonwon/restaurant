package com.restaurant.service;

import com.restaurant.dto.MainRestDto;
import com.restaurant.entity.Member;
import com.restaurant.entity.Rest;
import com.restaurant.entity.Star;
import com.restaurant.repository.StarRepository;
import com.restaurant.repository.StarRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StarService {

    private final StarRepository starRepository;
    private final StarRepositoryImpl sr;

    public int star(Star star) {
        int status = 0;
        Star findStar = starRepository.findByMemberAndRest(star.getMember(), star.getRest());
        System.out.println(findStar+"wqeqwe");
        if (findStar!=null){
            starRepository.delete(findStar);
            System.out.println("찜삭제");
        }else {
            starRepository.save(star);
            status=1;
            System.out.println("찜추가");
        }
        return status;
    }
    public Page<Star>findStar(Member member,Pageable pageable){
        Page<Star> star = sr.findStar(member, pageable);
        return star;
    }

}
