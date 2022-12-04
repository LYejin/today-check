package com.team.todaycheck.main.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails {

	@Id
	@GeneratedValue
	private Long userId;
	
	@Column(length=50 , nullable = false)
	private String id;
	
	@Column(length=50 , nullable = false)
	private String password;
	
	@ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();
	
	@Column(name = "ADMIN" , length = 10 , nullable = false)
	@Enumerated(EnumType.STRING)
	private Admin admin;
	
	@JsonIgnore
	@Builder.Default
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "userEntity" , orphanRemoval = true)
	private List<Post> post = new ArrayList<Post>();
	
	@JsonIgnore
	@Builder.Default
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "admin" , orphanRemoval = true)
	private List<Mission> mission = new ArrayList<Mission>();
	
	
	// 연관관계 편의 메소드
	public void addpost(Post postData) {
		post.add(postData);
		postData.setUserEntity(this);
	}
	public void addMission(Mission missionData) {
		mission.add(missionData);
		missionData.setAdmin(this);
	}
	
	public enum Admin {
		ADMin , GENERAL
	}
	
	public String getRoleKey(){
        return this.roles.get(0);
    }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream()
				.map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
	}

	@Override
	public String getUsername() {
		return id;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}
