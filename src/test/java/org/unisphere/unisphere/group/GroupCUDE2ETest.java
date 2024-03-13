package org.unisphere.unisphere.group;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.unisphere.unisphere.auth.jwt.JwtTokenProvider;
import org.unisphere.unisphere.group.domain.Group;
import org.unisphere.unisphere.group.domain.GroupRole;
import org.unisphere.unisphere.group.dto.request.GroupAvatarUpdateRequestDto;
import org.unisphere.unisphere.group.dto.request.GroupCreateRequestDto;
import org.unisphere.unisphere.group.dto.request.GroupHomePageUpdateRequestDto;
import org.unisphere.unisphere.group.dto.response.GroupHomePageResponseDto;
import org.unisphere.unisphere.member.domain.Member;
import org.unisphere.unisphere.utils.E2EMvcTest;
import org.unisphere.unisphere.utils.JsonMatcher;
import org.unisphere.unisphere.utils.PersistenceHelper;
import org.unisphere.unisphere.utils.entity.TestGroup;
import org.unisphere.unisphere.utils.entity.TestGroupRegistration;
import org.unisphere.unisphere.utils.entity.TestMember;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GroupCUDE2ETest extends E2EMvcTest {

	private final String BASE_URL = "/api/v1/groups";
	private PersistenceHelper persistenceHelper;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	private String token;

	@BeforeEach
	public void setup(WebApplicationContext webApplicationContext) {
		super.setup(webApplicationContext);
		persistenceHelper = PersistenceHelper.start(em);
	}

	@Nested
	class CreateGroup {

		private final String URL = BASE_URL;

		@BeforeEach
		public void setup() {
			loginMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			token = jwtTokenProvider.createCommonAccessToken(loginMember.getId()).getTokenValue();
		}

		@Test
		@DisplayName("이미 존재하는 그룹 이름으로 그룹 생성 요청 시 409 Conflict 응답")
		public void createGroup_AlreadyExistGroupName_400BadRequest() throws Exception {
			// given
			String groupName = "group1";
			persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.name(groupName)
							.ownerMember(loginMember)
							.build()
							.asEntity()
			);

			// when
			MockHttpServletRequestBuilder request = post(URL)
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupCreateRequestDto.builder()
									.name(groupName)
									.summary("summary")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isConflict());
		}

		@Test
		@DisplayName("그룹명이 빈 문자열인 그룹 생성 요청 시, 400 Bad Request 응답")
		public void createGroup_EmptyGroupName_400BadRequest() throws Exception {
			// given
			String emptyGroupName = "";

			// when
			MockHttpServletRequestBuilder request = post(URL)
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupCreateRequestDto.builder()
									.name(emptyGroupName)
									.summary("summary")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("그룹명 길이가 1인 그룹 생성 요청 시, 400 Bad Request 응답")
		public void createGroup_ShortGroupName_400BadRequest() throws Exception {
			// given
			String shortGroupName = "a";

			// when
			MockHttpServletRequestBuilder request = post(URL)
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupCreateRequestDto.builder()
									.name(shortGroupName)
									.summary("summary")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("그룹명 길이가 17인 그룹 생성 요청 시, 400 Bad Request 응답")
		public void createGroup_LongGroupName_400BadRequest() throws Exception {
			// given
			String longGroupName = "12345678901234567";

			// when
			MockHttpServletRequestBuilder request = post(URL)
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupCreateRequestDto.builder()
									.name(longGroupName)
									.summary("summary")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("그룹명에 특수문자가 포함된 그룹 생성 요청 시, 400 Bad Request 응답")
		public void createGroup_InvalidGroupName_400BadRequest() throws Exception {
			// given
			String invalidGroupName = "group!";

			// when
			MockHttpServletRequestBuilder request = post(URL)
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupCreateRequestDto.builder()
									.name(invalidGroupName)
									.summary("summary")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("허용되지 않은 로고 이미지 확장자로 그룹 생성 요청 시, 400 Bad Request 응답")
		public void createGroup_InvalidLogoImageExtension_400BadRequest() throws Exception {
			// given
			String invalidLogoImageExtension = "logo-images/" + UUID.randomUUID() + "/logo.bmp";

			// when
			MockHttpServletRequestBuilder request = post(URL)
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupCreateRequestDto.builder()
									.name("group1")
									.summary("summary")
									.preSignedLogoImageUrl(invalidLogoImageExtension)
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("정상적인 그룹 생성 요청 시, 201 Created 응답")
		public void createGroup_ValidRequest_201Created() throws Exception {
			// given
			String groupName = "group1";

			// when
			MockHttpServletRequestBuilder request = post(URL)
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupCreateRequestDto.builder()
									.name(groupName)
									.summary("summary")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);
			persistenceHelper.flushAndClear();

			// then
			resultActions.andExpect(status().isCreated())
					.andDo(ignore -> {
						Group queriedGroup = em.createQuery(
										"select g from group_entity g join fetch g.groupRegistrations gr",
										Group.class)
								.getSingleResult();
						assertThat(queriedGroup.getName()).isEqualTo(groupName);
						assertThat(queriedGroup.getOwnerMember().getId()).isEqualTo(
								loginMember.getId());
						assertThat(queriedGroup.getGroupRegistrations().size()).isEqualTo(1);
						assertThat(queriedGroup.getGroupRegistrations().get(0).getRole()).isEqualTo(
								GroupRole.OWNER);
					});
		}
	}

	@Nested
	class UpdateGroupAvatar {

		private final String URL = BASE_URL;

		@BeforeEach
		public void setup() {
			loginMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			token = jwtTokenProvider.createCommonAccessToken(loginMember.getId()).getTokenValue();
		}

		@Test
		@DisplayName("존재하지 않는 그룹에 대한 그룹 아바타 변경 요청 시, 404 Not Found 응답")
		public void updateGroupAvatar_NotExistGroup_404NotFound() throws Exception {
			// given
			Long notExistGroupId = 1L;

			// when
			MockHttpServletRequestBuilder request = patch(URL + "/" + notExistGroupId + "/avatar")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupAvatarUpdateRequestDto.builder()
									.name("group1")
									.preSignedAvatarImageUrl(
											"avatar-images/" + UUID.randomUUID() + "/avatar.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isNotFound());
		}

		@Test
		@DisplayName("존재하는 그룹명으로 그룹 아바타 변경 요청 시, 409 Conflict 응답")
		public void updateGroupAvatar_AlreadyExistGroupName_409Conflict() throws Exception {
			// given
			String groupName = "group1";
			String preSignedAvatarImageUrl = "avatar-images/" + UUID.randomUUID() + "/avatar.png";
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.asDefaultEntity(loginMember)
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(loginMember, group)
			);
			Group anotherGroup = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.name(groupName)
							.ownerMember(loginMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(loginMember, anotherGroup)
			);

			// when
			MockHttpServletRequestBuilder request = patch(URL + "/" + group.getId() + "/avatar")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupAvatarUpdateRequestDto.builder()
									.name(groupName)
									.preSignedAvatarImageUrl(preSignedAvatarImageUrl)
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isConflict());
		}

		@Test
		@DisplayName("그룹 멤버가 아닌 멤버가 그룹 아바타 변경 요청 시, 403 Forbidden 응답")
		public void updateGroupAvatar_NotGroupAdmin_403Forbidden() throws Exception {
			// given
			String groupName = "group1";
			String preSignedAvatarImageUrl = "avatar-images/" + UUID.randomUUID() + "/avatar.png";
			Member anotherMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(anotherMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(anotherMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = patch(URL + "/" + group.getId() + "/avatar")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupAvatarUpdateRequestDto.builder()
									.name(groupName)
									.preSignedAvatarImageUrl(preSignedAvatarImageUrl)
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("가입 승인된 일반 멤버가 그룹 아바타 변경 요청 시, 403 Forbidden 응답")
		public void updateGroupAvatar_ApprovedCommonMember_403Forbidden() throws Exception {
			// given
			String groupName = "group1";
			String preSignedAvatarImageUrl = "avatar-images/" + UUID.randomUUID() + "/avatar.png";
			Member anotherMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(anotherMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(anotherMember, group)
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asApprovedCommonRegistration(loginMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = patch(URL + "/" + group.getId() + "/avatar")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupAvatarUpdateRequestDto.builder()
									.name(groupName)
									.preSignedAvatarImageUrl(preSignedAvatarImageUrl)
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("가입 승인되지 않은 일반 멤버가 그룹 아바타 변경 요청 시, 403 Forbidden 응답")
		public void updateGroupAvatar_NotApprovedCommonMember_403Forbidden() throws Exception {
			// given
			String groupName = "group1";
			String preSignedAvatarImageUrl = "avatar-images/" + UUID.randomUUID() + "/avatar.png";
			Member anotherMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(anotherMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(anotherMember, group)
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asNotApprovedCommonRegistration(loginMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = patch(URL + "/" + group.getId() + "/avatar")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupAvatarUpdateRequestDto.builder()
									.name(groupName)
									.preSignedAvatarImageUrl(preSignedAvatarImageUrl)
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("정상적인 그룹 아바타 변경 요청 시, 그룹명은 변경되지 않고 200 OK 응답")
		public void updateGroupAvatar_ValidRequest_204NoContent() throws Exception {
			// given
			String beforeImageUrl = "avatar-images/BEFORE/avatar.png";
			String afterImageUrl = "avatar-images/AFTER/avatar.png";
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(loginMember)
							.avatarImageUrl(beforeImageUrl)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(loginMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = patch(URL + "/" + group.getId() + "/avatar")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupAvatarUpdateRequestDto.builder()
									.preSignedAvatarImageUrl(afterImageUrl)
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);
			persistenceHelper.flushAndClear();

			// then
			resultActions.andExpect(status().isOk())
					.andDo(ignore -> {
						Group queriedGroup = em.find(Group.class, group.getId());
						assertThat(queriedGroup.getAvatarImageUrl()).isEqualTo(afterImageUrl);
						assertThat(queriedGroup.getName()).isNotNull();
					});
		}

		@Test
		@DisplayName("소유자가 그룹 아바타 변경 요청 시, 204 No Content 응답")
		public void updateGroupAvatar_Owner_204NoContent() throws Exception {
			// given
			String groupName = "group1";
			String preSignedAvatarImageUrl = "avatar-images/" + UUID.randomUUID() + "/avatar.png";
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(loginMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(loginMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = patch(URL + "/" + group.getId() + "/avatar")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupAvatarUpdateRequestDto.builder()
									.name(groupName)
									.preSignedAvatarImageUrl(preSignedAvatarImageUrl)
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);
			persistenceHelper.flushAndClear();

			// then
			resultActions.andExpect(status().isOk())
					.andDo(ignore -> {
						Group queriedGroup = em.find(Group.class, group.getId());
						assertThat(queriedGroup.getAvatarImageUrl()).isNotNull();
						assertThat(queriedGroup.getName()).isEqualTo(groupName);
					});
		}

		@Test
		@DisplayName("관리자가 그룹 아바타 변경 요청 시, 204 No Content 응답")
		public void updateGroupAvatar_Admin_204NoContent() throws Exception {
			// given
			String groupName = "group1";
			String preSignedAvatarImageUrl = "avatar-images/" + UUID.randomUUID() + "/avatar.png";
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(persistenceHelper.persistAndReturn(
									TestMember.asDefaultEntity()
							))
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asAdminRegistration(loginMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = patch(URL + "/" + group.getId() + "/avatar")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupAvatarUpdateRequestDto.builder()
									.name(groupName)
									.preSignedAvatarImageUrl(preSignedAvatarImageUrl)
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);
			persistenceHelper.flushAndClear();

			// then
			resultActions.andExpect(status().isOk())
					.andDo(ignore -> {
						Group queriedGroup = em.find(Group.class, group.getId());
						assertThat(queriedGroup.getAvatarImageUrl()).isNotNull();
						assertThat(queriedGroup.getName()).isEqualTo(groupName);
					});
		}
	}

	@Nested
	class PutGroupHomePage {

		private final String URL = BASE_URL;

		@BeforeEach
		public void setup() {
			loginMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			token = jwtTokenProvider.createCommonAccessToken(loginMember.getId()).getTokenValue();
		}

		@Test
		@DisplayName("존재하지 않는 그룹에 대한 그룹 홈페이지 변경 요청 시, 404 Not Found 응답")
		public void putGroupHomePage_NotExistGroup_404NotFound() throws Exception {
			// given
			long notExistGroupId = 9999L;

			// when
			MockHttpServletRequestBuilder request = put(
					URL + "/" + notExistGroupId + "/home-page")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupHomePageUpdateRequestDto.builder()
									.content("content")
									.email("example@gmail.com")
									.groupSiteUrl("https://unisphere.org/group/1")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isNotFound());
		}

		@Test
		@DisplayName("지원되지 않는 로고 이미지 확장자로 그룹 홈페이지 변경 요청 시, 400 Bad Request 응답")
		public void putGroupHomePage_InvalidLogoImageExtension_400BadRequest() throws Exception {
			// given
			String invalidLogoImageExtension = "logo-images/" + UUID.randomUUID() + "/logo.bmp";
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(loginMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(loginMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = put(
					URL + "/" + group.getId() + "/home-page")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupHomePageUpdateRequestDto.builder()
									.content("content")
									.email("example@gmail.com")
									.groupSiteUrl("https://unisphere.org/group/1")
									.preSignedLogoImageUrl(
											invalidLogoImageExtension)
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("단체 소개가 1025자인 그룹 홈페이지 변경 요청 시, 400 Bad Request 응답")
		public void putGroupHomePage_LongContent_400BadRequest() throws Exception {
			// given
			String longContent = "a" .repeat(1025);
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(loginMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(loginMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = put(
					URL + "/" + group.getId() + "/home-page")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupHomePageUpdateRequestDto.builder()
									.content(longContent)
									.email("example@gmail.com")
									.groupSiteUrl("https://unisphere.org/group/1")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("단체 이메일 주소가 65자인 그룹 홈페이지 변경 요청 시, 400 Bad Request 응답")
		public void putGroupHomePage_LongEmail_400BadRequest() throws Exception {
			// given
			String longEmail = "a" .repeat(65);
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(loginMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(loginMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = put(
					URL + "/" + group.getId() + "/home-page")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupHomePageUpdateRequestDto.builder()
									.content("content")
									.email(longEmail)
									.groupSiteUrl("https://unisphere.org/group/1")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("단체 이메일 형식이 올바르지 않은 그룹 홈페이지 변경 요청 시, 400 Bad Request 응답")
		public void putGroupHomePage_InvalidEmail_1_400BadRequest() throws Exception {
			// given
			String invalidEmail = "example.com";
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(loginMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(loginMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = put(
					URL + "/" + group.getId() + "/home-page")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupHomePageUpdateRequestDto.builder()
									.content("content")
									.email(invalidEmail)
									.groupSiteUrl("https://unisphere.org/group/1")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("단체 이메일 형식이 올바르지 않은 그룹 홈페이지 변경 요청 시, 400 Bad Request 응답")
		public void putGroupHomePage_InvalidEmail_2_400BadRequest() throws Exception {
			// given
			String invalidEmail = "example@com";
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(loginMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(loginMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = put(
					URL + "/" + group.getId() + "/home-page")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupHomePageUpdateRequestDto.builder()
									.content("content")
									.email(invalidEmail)
									.groupSiteUrl("https://unisphere.org/group/1")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("단체 이메일 형식이 올바르지 않은 그룹 홈페이지 변경 요청 시, 400 Bad Request 응답")
		public void putGroupHomePage_InvalidEmail_3_400BadRequest() throws Exception {
			// given
			String invalidEmail = "example@.com";
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(loginMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(loginMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = put(
					URL + "/" + group.getId() + "/home-page")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupHomePageUpdateRequestDto.builder()
									.content("content")
									.email(invalidEmail)
									.groupSiteUrl("https://unisphere.org/group/1")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("그룹 멤버가 아닌 멤버가 그룹 홈페이지 변경 요청 시, 403 Forbidden 응답")
		public void putGroupHomePage_NotGroupAdmin_403Forbidden() throws Exception {
			// given
			Member ownerMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(ownerMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(ownerMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = put(
					URL + "/" + group.getId() + "/home-page")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupHomePageUpdateRequestDto.builder()
									.content("content")
									.email("example@gmail.com")
									.groupSiteUrl("https://unisphere.org/group/1")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("가입 승인된 일반 멤버가 그룹 홈페이지 변경 요청 시, 403 Forbidden 응답")
		public void putGroupHomePage_ApprovedCommonMember_403Forbidden() throws Exception {
			// given
			Member ownerMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(ownerMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(ownerMember, group)
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asApprovedCommonRegistration(loginMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = put(
					URL + "/" + group.getId() + "/home-page")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupHomePageUpdateRequestDto.builder()
									.content("content")
									.email("example@gmail.com")
									.groupSiteUrl("https://unisphere.org/group/1")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("가입 승인되지 않은 일반 멤버가 그룹 홈페이지 변경 요청 시, 403 Forbidden 응답")
		public void putGroupHomePage_NotApprovedCommonMember_403Forbidden() throws Exception {
			// given
			Member ownerMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(ownerMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(ownerMember, group)
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asNotApprovedCommonRegistration(loginMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = put(
					URL + "/" + group.getId() + "/home-page")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupHomePageUpdateRequestDto.builder()
									.content("content")
									.email("example@gmail.com")
									.groupSiteUrl("https://unisphere.org/group/1")
									.preSignedLogoImageUrl(
											"logo-images/" + UUID.randomUUID() + "/logo.png")
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isForbidden());
		}

		@Test
		@DisplayName("정상적인 그룹 홈페이지 변경 요청 시, 200 OK 응답")
		public void putGroupHomePage_ValidRequest_200OK() throws Exception {
			// given
			String content = "content";
			String email = "example@gmail.com";
			String groupSiteUrl = "https://unisphere.org/group/1";
			Group group = persistenceHelper.persistAndReturn(
					TestGroup.builder()
							.ownerMember(loginMember)
							.build()
							.asEntity()
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(loginMember, group)
			);

			// when
			MockHttpServletRequestBuilder request = put(
					URL + "/" + group.getId() + "/home-page")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.content(objectMapper.writeValueAsString(
							GroupHomePageUpdateRequestDto.builder()
									.content(content)
									.email(email)
									.groupSiteUrl(groupSiteUrl)
									.build()))
					.contentType(MediaType.APPLICATION_JSON);
			ResultActions resultActions = mockMvc.perform(request);
			persistenceHelper.flushAndClear();

			// then
			JsonMatcher jsonMatcher = JsonMatcher.create();
			resultActions.andExpect(status().isOk())
					.andExpect(jsonMatcher.get(GroupHomePageResponseDto.Fields.content)
							.isEquals(content))
					.andExpect(
							jsonMatcher.get(GroupHomePageResponseDto.Fields.email).isEquals(email))
					.andExpect(jsonMatcher.get(GroupHomePageResponseDto.Fields.groupSiteUrl)
							.isEquals(groupSiteUrl));
		}
	}
}
