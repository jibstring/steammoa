package com.ssafy.backend.api.service;

import com.ssafy.backend.api.request.PartyPostReq;
import com.ssafy.backend.api.request.PartyPutReq;
import com.ssafy.backend.db.entity.game.GameDTO;
import com.ssafy.backend.db.entity.game.GamelistDTO;
import com.ssafy.backend.db.entity.party.*;
import com.ssafy.backend.db.repository.UserRepository;
import com.ssafy.backend.db.repository.game.GameRepository;
import com.ssafy.backend.db.repository.party.PartyRepository;
import com.ssafy.backend.db.repository.party.PartyTagRepository;
import com.ssafy.backend.db.repository.party.PtagStorageRepository;
import com.ssafy.backend.db.repository.party.PuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("partyService")
public class PartyServiceImpl implements PartyService{
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PtagStorageRepository ptagStorageRepository;
    @Autowired
    private PartyTagRepository partyTagRepository;
    @Autowired
    private PuserRepository puserRepository;

    // 파티 전체 목록
    @Override
    @Transactional
    public List<PartylistDTO> getPartyList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        List<PartylistDTO> resultlist = new ArrayList<>();
        partyRepository.findAll().forEach(Party->resultlist.add(new PartylistDTO(Party)));
        return resultlist;
    }

    // 파티 검색+필터+정렬 기반 목록
    /*
        필터링: 게임 이름, 파티 상태, 게임 장르
        정렬: 파티 생성 최근순 (디폴트), 마감 날짜 가까운 순, 게임 이름 순
     */
    @Override
    @Transactional
    public List<PartylistDTO> searchPartyList(int page, String searchString, String[] tags, String partyStatus, String sortString) {
        Pageable pageable = PageRequest.of(page, 10);
        List<PartylistDTO> resultlist = new ArrayList<>();
        partyRepository.findAllPartyByFilter(searchString, tags, partyStatus, sortString, pageable).forEach(Party->resultlist.add(new PartylistDTO(Party)));
        return resultlist;
    }

    // 파티 생성
    /*
        파티 코드
        모집 중: 1
        모집 완료: 2
        플레이 중: 3
        플레이 완료: 4
        모집 실패: 5
     */
    @Override
    @Transactional
    public boolean createParty(PartyPostReq partyInfo) {
        Party party = new Party();

        party.setGame(gameRepository.findByGameId(Long.parseLong(partyInfo.getGame_id())));
        party.setTitle(partyInfo.getParty_title());
        party.setMaxPlayer(Integer.parseInt(partyInfo.getMax_player()));
        party.setCurPlayer(1);
        party.setDescription(partyInfo.getParty_description());
        party.setStartTime(LocalDateTime.parse(partyInfo.getStart_time()));
        party.setChatLink(partyInfo.getChat_link());

        Pstatus pstatus = new Pstatus();
        pstatus.setContent("1");
        party.setPstatus(pstatus);

        PartyTag partyTag;
        for (String tag:partyInfo.getPartyTags()) {
            partyTag = new PartyTag();
            partyTag.setPtagStorage(ptagStorageRepository.findByContent(tag));
            party.addPartyTag(partyTag);
        }

        Puser puser = new Puser();
        puser.setPuserId(Long.parseLong(partyInfo.getUser_id()));
        puser.setLeader(true);
        party.addPuser(puser);

        return true;
    }

    // 파티 상세 조회
    @Override
    @Transactional
    public PartyDTO getPartyDetail(Long partyId) {
        PartyDTO partyDetail = new PartyDTO(partyRepository.findByPartyId(partyId));
        return partyDetail;
    }

    // 파티 수정
    @Override
    @Transactional
    public boolean updateParty(Long partyId, PartyPutReq partyInfo) {
        Party party = partyRepository.findByPartyId(partyId);

        party.setDescription(partyInfo.getParty_description());
        party.setChatLink(partyInfo.getChat_link());

        // 태그 수정
        for (PartyTag pt: party.getPartyTags()) {
            partyTagRepository.delete(pt);
        }

        PartyTag partyTag;
        for (String tag:partyInfo.getPartyTags()) {
            partyTag = new PartyTag();
            partyTag.setPtagStorage(ptagStorageRepository.findByContent(tag));
            party.addPartyTag(partyTag);
        }

        // 멤버 수정
        // 파티장만 놔두고 다 지운 다음에, 멤버 추가(이미 있는 멤버는 추가 안 함)
        for (Puser u: party.getPusers()) {
            if(!u.isLeader())
                puserRepository.delete(u);
        }

        Puser puser;
        for (String id:partyInfo.getPartyUsers()) {
            if(party.getPusers().contains(puserRepository.findById(Long.parseLong(id))))
                continue;

            puser = new Puser();
            puser.setPuserId(Long.parseLong(id));
            party.addPuser(puser);
        }

        party.setCurPlayer(partyInfo.getPartyUsers().length);

        // 파티 상태 수정
        party.getPstatus().setContent(partyInfo.getParty_status());

        partyRepository.save(party);

        return true;
    }

    // 파티 삭제
    @Override
    @Transactional
    public boolean deleteParty(Long partyId) {
        try{
            partyRepository.delete(partyRepository.findByPartyId(partyId));
            System.out.println("Party 삭제 요청 성공");
            return true;
        }catch (Exception e){
            System.out.println("Party 삭제 요청 실패");
            return false;
        }
    }
}
