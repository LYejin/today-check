package com.team.todaycheck.main.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.security.auth.login.AccountNotFoundException;
import javax.transaction.Transactional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.team.todaycheck.main.DTO.MessageDTO;
import com.team.todaycheck.main.DTO.MissionCertificationDTO;
import com.team.todaycheck.main.DTO.MissionDTO;
import com.team.todaycheck.main.DTO.ParticipantDTO;
import com.team.todaycheck.main.DTO.ParticipantsMissionDTO;
import com.team.todaycheck.main.DTO.ProfileMissionDTO;
import com.team.todaycheck.main.entity.Mission;
import com.team.todaycheck.main.entity.MissionCertification;
import com.team.todaycheck.main.entity.ParticipantsMission;
import com.team.todaycheck.main.entity.RefreshToken;
import com.team.todaycheck.main.entity.UserEntity;
import com.team.todaycheck.main.repository.IMissionRepository;
import com.team.todaycheck.main.repository.ParticipantMissionRepository;
import com.team.todaycheck.main.repository.ProfileRepository;
import com.team.todaycheck.main.repository.UserRepository;
import com.team.todaycheck.main.service.IMissionService;
import com.team.todaycheck.main.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MissionService implements IMissionService {
	private final IMissionRepository missionRepository;
	private final UserRepository userRepository;
	private final ParticipantMissionRepository partMissionRepository;
	private final ProfileRepository profileRepos;
	private final JwtService jwtService;
	
	private String fileDir = "C:\\devtool\\upload\\";
	/*
	@PostConstruct
	void init() throws Exception {
		if (userRepository.count() < 3) {
			loginService.createId(RegistryDTO.builder()
					.id("demo@example.com")
					.password("")
					.build());
			
			loginService.createId(RegistryDTO.builder()
					.id("demo2@example.com")
					.password("")
					.build());
			
			loginService.createId(RegistryDTO.builder()
					.id("demo3@example.com")
					.password("")
					.build());
		}
		
		
		if (partMissionRepository.count() == 0) {
		
			UserEntity user = userRepository.findById("demo@example.com");
			// demo data
			for (int i = 0; i < 10; i++) {
				List<UserEntity> participants = new ArrayList<>();
				participants.add(UserEntity.builder()
						.userId(2L).build());
				participants.add(UserEntity.builder()
						.userId(3L).build());
				
				LocalDateTime endDate = LocalDateTime.now();
				endDate.plusMonths(1);
					
				Mission mission = Mission.builder()
						.title("go gym everyday")
						.content("to go gym every day in 2 months!")
						.thumbnail("https://via.placeholder.com/350x150")
						.admin(UserEntity.builder().userId(1L).build())
						.participants(null)
						.startDate(LocalDateTime.now())
						.endDate(endDate)
						.build(); 
				
				ParticipantsMission result = ParticipantsMission.builder()
						.mission(mission)
						.participants(user)
						.build();
				
				partMissionRepository.save(result);
			}
		}
	}
	*/
	
	@Override
	public MissionDTO save(MissionDTO dto) {
		Mission result = missionRepository.save(toEntity(dto));
		return fromEntity(result);
	}
	
	@Override
	public List<ParticipantsMissionDTO> findAll() {
		List<ParticipantsMission> result = partMissionRepository.findAllMission();
		List<ParticipantsMissionDTO> list = new ArrayList<ParticipantsMissionDTO>();
		File imageFile;
		HttpHeaders header = new HttpHeaders();
		
		for(ParticipantsMission data : result) {
			List<MissionCertification> certList = data.getMissionCertification();				
			
			List<MissionCertificationDTO> dto = new ArrayList<MissionCertificationDTO>();
			
			for(MissionCertification cert : certList) {
				ResponseEntity<byte[]> imageData;
				
				imageFile = new File(fileDir + cert.getImage());
				try {
					if(Files.probeContentType(imageFile.toPath()) != null) header.set("Content-Type" , Files.probeContentType(imageFile.toPath()));
					imageData = (new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(imageFile) , header , HttpStatus.OK));
				} catch (IOException e) {
					imageData = (null);
				}
				
				dto.add(MissionCertificationDTO.builder()
						.date(cert.getCheckDate())
						.keys(cert.getKeys())
						.userName(cert.getUserName())
						.image(imageData)
						.build());
			}
			list.add(ParticipantsMissionDTO.builder()
					.keys(data.getKeys())
					.mission(data.getMission())
					.participants(data.getParticipants())
					.missionCertification(dto)
					.build());
		}
		return list;
	}
	

	public static MissionDTO fromEntity(Mission mission) {
		
		return MissionDTO.builder()
				.id(mission.getId())
				.admin(ParticipantDTO.builder()
						.id(mission.getAdmin().getUserId())
						.email(mission.getAdmin().getId())
						.name(mission.getAdmin().getId())
						.avater("https://firebasestorage.googleapis.com/v0/b/instagram-clone-eb58a.appspot.com/o/default-profile.png?alt=media&token=30f8935d-0920-4ba7-960d-bcf35a0d26aa")
						.build())
				.postTitle(mission.getTitle())
				.postContent(mission.getContent())
				.postPicture(mission.getThumbnail())
				.participants(null)
				.startDate(mission.getStartDate())
				.endDate(mission.getEndDate())
				.build();
	}
	
	public static Mission toEntity(MissionDTO dto) {
		return Mission.builder()
				.id(dto.getId())
				.title(dto.getPostTitle())
				.admin(UserEntity.builder().userId(dto.getAdmin().getId()).build())
				.content(dto.getPostContent())
				.thumbnail(dto.getPostPicture())
				.participants(null)
				.startDate(dto.getStartDate())
				.endDate(dto.getEndDate())
				.build();
	}

	@Override
	public MissionDTO addParticipant(MissionDTO dto, ParticipantDTO participant) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ParticipantsMissionDTO> findMission(Long i) {
		List<ParticipantsMission> result = partMissionRepository.findMission(i);
		List<ParticipantsMissionDTO> list = new ArrayList<ParticipantsMissionDTO>();
		File imageFile;
		HttpHeaders header = new HttpHeaders();
		
		for(ParticipantsMission data : result) {
			List<MissionCertification> certList = data.getMissionCertification();				
			
			List<MissionCertificationDTO> dto = new ArrayList<MissionCertificationDTO>();
			
			for(MissionCertification cert : certList) {
				ResponseEntity<byte[]> imageData;
				
				imageFile = new File(fileDir + cert.getImage());
				try {
					if(Files.probeContentType(imageFile.toPath()) != null) header.set("Content-Type" , Files.probeContentType(imageFile.toPath()));
					imageData = (new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(imageFile) , header , HttpStatus.OK));
				} catch (IOException e) {
					imageData = (null);
				}
				
				dto.add(MissionCertificationDTO.builder()
						.date(cert.getCheckDate())
						.keys(cert.getKeys())
						.userName(cert.getUserName())
						.image(imageData)
						.build());
			}
			list.add(ParticipantsMissionDTO.builder()
					.keys(data.getKeys())
					.mission(data.getMission())
					.participants(data.getParticipants())
					.missionCertification(dto)
					.build());
		}
		return list;
		
	}

	@Override
	public Mission findById(Long id) {
		return missionRepository.findById(id).get();
	}

	@Override
	public ResponseEntity leaveMission(long id, String cookie) {
		Mission mission = findById(id);
    	if (mission == null) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    	}
    	
    	Optional<RefreshToken> o = jwtService.getRefreshToken(cookie);
    	
    	RefreshToken token = o.orElse(null);
    	if (token == null) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
    	
    	UserEntity user = userRepository.findById(token.getKeyEmail());
    	partMissionRepository.leaveMission(user.getUserId() , id);
    	
        return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<Object> joinMission(long id , String cookie) {
		Mission mission = findById(id);
    	
    	if (mission == null) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    	}
    	
    	Optional<RefreshToken> o = jwtService.getRefreshToken(cookie);
    	
    	RefreshToken token = o.orElse(null);
    	if (token == null) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
    	
    	UserEntity user = userRepository.findById(token.getKeyEmail());
    	List<ProfileMissionDTO> joinMission = profileRepos.getJoinMissionList(user.getId());
    	
    	for (ProfileMissionDTO participant : joinMission) {
    		if (participant.getId() == id) {
    			return ResponseEntity.status(HttpStatus.CONFLICT).build();
    		}
    	}
    	
    	ParticipantsMission participant = ParticipantsMission.builder()
    			.mission(mission)
    			.participants(user)
    			.build();
    	
    	partMissionRepository.save(participant);
    	
    	return ResponseEntity.ok().build();
	}

	@Override
	public MessageDTO certifyMission(Long id, MultipartFile image , String cookie) throws AccountNotFoundException, IllegalStateException, IOException {
		
		Optional<RefreshToken> o = jwtService.getRefreshToken(cookie);
    	
    	RefreshToken token = o.orElse(null);
    	if (token == null) {
    		throw new AccountNotFoundException("계정을 찾을 수 없습니다.");
    	}
    	UserEntity user = userRepository.findById(token.getKeyEmail());
    	ParticipantsMission mission = partMissionRepository.findOneMission(user.getId(), id);
    	
    	if(image != null) {
    		String origName = image.getOriginalFilename();
			String uuid = UUID.randomUUID().toString(); // 중복을 처리하기 위한 UUID
			String extension = origName.substring(origName.lastIndexOf(".")); // 확장자 추출
			String savedName = uuid + extension;
			
			image.transferTo(new File(fileDir + savedName)); // 파일 저장
			
			MissionCertification result = MissionCertification.builder()
				.image(savedName)
				.userName(user.getId())
				.build();
			
			mission.addMissionCertification(result);
			
			
    	}
    	
		return MessageDTO.builder()
				.code("1")
				.message("인증이 완료되었습니다")
				.build();
	}

	@Override
	public MissionCertificationDTO getCertifyMission(Long id) {
		return null;
	}
}