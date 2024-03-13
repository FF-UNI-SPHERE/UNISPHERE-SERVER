package org.unisphere.unisphere.group;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.unisphere.unisphere.auth.jwt.JwtTokenProvider;
import org.unisphere.unisphere.group.domain.Group;
import org.unisphere.unisphere.group.domain.GroupRole;
import org.unisphere.unisphere.group.dto.response.GroupListResponseDto;
import org.unisphere.unisphere.group.dto.response.GroupMemberListResponseDto;
import org.unisphere.unisphere.member.domain.Member;
import org.unisphere.unisphere.utils.E2EMvcTest;
import org.unisphere.unisphere.utils.JsonMatcher;
import org.unisphere.unisphere.utils.PersistenceHelper;
import org.unisphere.unisphere.utils.entity.TestGroup;
import org.unisphere.unisphere.utils.entity.TestGroupRegistration;
import org.unisphere.unisphere.utils.entity.TestMember;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GroupReadE2ETest extends E2EMvcTest {

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
	class GetAllGroups {

		private final String URL = BASE_URL + "/all";

		@BeforeEach
		public void setup() {
			loginMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			token = jwtTokenProvider.createCommonAccessToken(loginMember.getId()).getTokenValue();
		}

		@Test
		@DisplayName("전체 그룹 수가 0인 경우, 빈 리스트를 반환한다")
		public void testGetAllGroups_empty() throws Exception {
			// given
			long totalGroupCount = 0;

			// when
			MockHttpServletRequestBuilder request = get(URL)
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			JsonMatcher response = JsonMatcher.create();
			resultActions
					.andExpect(status().isOk())
					.andExpect(
							response.get(GroupListResponseDto.Fields.totalGroupCount)
									.isEquals(totalGroupCount))
					.andExpect(
							response.get(GroupListResponseDto.Fields.groups)
									.get("length()").isEquals(0));
		}

		@Test
		@DisplayName("전체 그룹 수가 size 보다 큰 경우, size 만큼의 그룹 리스트를 반환한다")
		public void testGetAllGroups() throws Exception {
			// given
			long totalGroupCount = 33;
			long page = 0;
			long size = 10;
			persistenceHelper.persistAndReturn(
					TestGroup.asDefaultEntities((int) totalGroupCount, loginMember)
			);

			// when
			MockHttpServletRequestBuilder request = get(URL)
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.param("page", String.valueOf(page))
					.param("size", String.valueOf(size));
			ResultActions resultActions = mockMvc.perform(request);

			// then
			JsonMatcher response = JsonMatcher.create();
			resultActions
					.andExpect(status().isOk())
					.andExpect(
							response.get(GroupListResponseDto.Fields.totalGroupCount)
									.isEquals(totalGroupCount))
					.andExpect(
							response.get(GroupListResponseDto.Fields.groups)
									.get("length()").isEquals(size));
		}

		@Test
		@DisplayName("전체 그룹 수가 size 보다 작은 경우, 전체 그룹 리스트를 반환한다")
		public void testGetAllGroups2() throws Exception {
			// given
			long totalGroupCount = 5;
			long page = 0;
			long size = 10;
			persistenceHelper.persistAndReturn(
					TestGroup.asDefaultEntities((int) totalGroupCount, loginMember)
			);

			// when
			MockHttpServletRequestBuilder request = get(URL)
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.param("page", String.valueOf(page))
					.param("size", String.valueOf(size));
			ResultActions resultActions = mockMvc.perform(request);

			// then
			JsonMatcher response = JsonMatcher.create();
			resultActions
					.andExpect(status().isOk())
					.andExpect(
							response.get(GroupListResponseDto.Fields.totalGroupCount)
									.isEquals(totalGroupCount))
					.andExpect(
							response.get(GroupListResponseDto.Fields.groups)
									.get("length()").isEquals(totalGroupCount));
		}
	}

	@Nested
	class GetMyGroups {

		private final String URL = BASE_URL + "/members/me";

		@BeforeEach
		public void setup() {
			loginMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			token = jwtTokenProvider.createCommonAccessToken(loginMember.getId()).getTokenValue();
		}

		@Test
		@DisplayName("내 그룹 수가 0인 경우, 빈 리스트를 반환한다")
		public void testGetMyGroups_empty() throws Exception {
			// given
			long totalGroupCount = 0;

			// when
			MockHttpServletRequestBuilder request = get(URL)
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			JsonMatcher response = JsonMatcher.create();
			resultActions
					.andExpect(status().isOk())
					.andExpect(
							response.get(GroupListResponseDto.Fields.totalGroupCount)
									.isEquals(totalGroupCount))
					.andExpect(
							response.get(GroupListResponseDto.Fields.groups)
									.get("length()").isEquals(0));
		}

		@Test
		@DisplayName("내가 속한 그룹만 존재")
		public void testGetMyGroups_onlyMyGroups() throws Exception {
			// given
			long totalGroupCount = 33;
			long page = 0;
			long size = 10;
			persistenceHelper.persistAndReturn(
							TestGroup.asDefaultEntities((int) totalGroupCount, loginMember)
					)
					.forEach(group -> persistenceHelper.persistAndReturn(
							TestGroupRegistration.asOwnerRegistration(loginMember, group)
					));

			// when
			MockHttpServletRequestBuilder request = get(URL)
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.param("page", String.valueOf(page))
					.param("size", String.valueOf(size));
			ResultActions resultActions = mockMvc.perform(request);

			// then
			JsonMatcher response = JsonMatcher.create();
			resultActions
					.andExpect(status().isOk())
					.andExpect(
							response.get(GroupListResponseDto.Fields.totalGroupCount)
									.isEquals(totalGroupCount))
					.andExpect(
							response.get(GroupListResponseDto.Fields.groups)
									.get("length()").isEquals(size));
		}

		@Test
		@DisplayName("내가 속하지 않은 그롭도 존재")
		public void testGetMyGroups_myGroupsAndOtherGroups() throws Exception {
			// given
			long totalMyGroupCount = 12;
			long totalOtherGroupCount = 6;
			long page = 0;
			long size = 10;
			persistenceHelper.persistAndReturn(
					TestGroup.asDefaultEntities((int) totalOtherGroupCount,
							persistenceHelper.persistAndReturn(TestMember.asDefaultEntity())
					)
			);
			persistenceHelper.persistAndReturn(
							TestGroup.asDefaultEntities((int) totalMyGroupCount, loginMember)
					)
					.forEach(group -> persistenceHelper.persistAndReturn(
							TestGroupRegistration.asOwnerRegistration(loginMember, group)
					));

			// when
			MockHttpServletRequestBuilder request = get(URL)
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.param("page", String.valueOf(page))
					.param("size", String.valueOf(size));
			ResultActions resultActions = mockMvc.perform(request);

			// then
			JsonMatcher response = JsonMatcher.create();
			resultActions
					.andExpect(status().isOk())
					.andExpect(
							response.get(GroupListResponseDto.Fields.totalGroupCount)
									.isEquals(totalMyGroupCount))
					.andExpect(
							response.get(GroupListResponseDto.Fields.groups)
									.get("length()").isEquals(size));
		}
	}

	@Nested
	class GetMemberGroups {

		private final String URL = BASE_URL + "/members";

		@BeforeEach
		public void setup() {
			loginMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			token = jwtTokenProvider.createCommonAccessToken(loginMember.getId()).getTokenValue();
		}

		@Test
		@DisplayName("해당 멤버가 존재하지 않는 경우, 404를 응답한다")
		public void testGetMemberGroups_nonExists() throws Exception {
			// given
			long nonExistMemberId = 9999L;

			// when
			MockHttpServletRequestBuilder request = get(URL + "/" + nonExistMemberId)
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isNotFound());
		}

		@Test
		@DisplayName("해당 멤버가 속한 그룹이 없는 경우, 빈 리스트를 반환한다")
		public void testGetMemberGroups_empty() throws Exception {
			// given
			Member targetMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			long totalGroupCount = 0;

			// when
			MockHttpServletRequestBuilder request = get(URL + "/" + targetMember.getId())
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			JsonMatcher response = JsonMatcher.create();
			resultActions
					.andExpect(status().isOk())
					.andExpect(
							response.get(GroupListResponseDto.Fields.totalGroupCount)
									.isEquals(totalGroupCount))
					.andExpect(
							response.get(GroupListResponseDto.Fields.groups)
									.get("length()").isEquals(0));
		}

		@Test
		@DisplayName("해당 멤버가 속한 그룹이 존재하는 경우, 해당 멤버가 속한 그룹 리스트를 반환한다")
		public void testGetMemberGroups() throws Exception {
			// given
			Member targetMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			long totalGroupCount = 15;
			long page = 0;
			long size = 10;
			persistenceHelper.persistAndReturn(
							TestGroup.asDefaultEntities((int) totalGroupCount, targetMember)
					)
					.forEach(group -> persistenceHelper.persistAndReturn(
							TestGroupRegistration.asOwnerRegistration(targetMember, group)
					));

			// when
			MockHttpServletRequestBuilder request = get(URL + "/" + targetMember.getId())
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token)
					.param("page", String.valueOf(page))
					.param("size", String.valueOf(size));
			ResultActions resultActions = mockMvc.perform(request);

			// then
			JsonMatcher response = JsonMatcher.create();
			resultActions
					.andExpect(status().isOk())
					.andExpect(
							response.get(GroupListResponseDto.Fields.totalGroupCount)
									.isEquals(totalGroupCount))
					.andExpect(
							response.get(GroupListResponseDto.Fields.groups)
									.get("length()").isEquals(size));
		}
	}

	@Nested
	class GetGroupMembers {

		private final String URL = BASE_URL;

		@BeforeEach
		public void setup() {
			loginMember = persistenceHelper.persistAndReturn(
					TestMember.asDefaultEntity()
			);
			token = jwtTokenProvider.createCommonAccessToken(loginMember.getId()).getTokenValue();
		}

		@Test
		@DisplayName("해당 그룹이 존재하지 않는 경우, 404를 응답한다")
		public void testGetGroupMembers_nonExists() throws Exception {
			// given
			long nonExistGroupId = 9999L;

			// when
			MockHttpServletRequestBuilder request = get(URL + "/" + nonExistGroupId + "/members")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			resultActions.andExpect(status().isNotFound());
		}

		@Test
		@DisplayName("해당 그룹에 소유자만 속한 경우, 소유자만 반환한다")
		public void testGetGroupMembers_onlyOwner() throws Exception {
			// given
			long totalMemberCount = 1;
			Group targetGroup = persistenceHelper.persistAndReturn(
					TestGroup.asDefaultEntity(loginMember)
			);
			persistenceHelper.persistAndReturn(
					TestGroupRegistration.asOwnerRegistration(loginMember, targetGroup)
			);

			// when
			MockHttpServletRequestBuilder request = get(
					URL + "/" + targetGroup.getId() + "/members")
					.header(AUTHORIZATION_VALUE, BEARER_VALUE + token);
			ResultActions resultActions = mockMvc.perform(request);

			// then
			JsonMatcher response = JsonMatcher.create();
			resultActions
					.andExpect(status().isOk())
					.andExpect(
							response.get(GroupMemberListResponseDto.Fields.totalMemberCount)
									.isEquals(totalMemberCount))
					.andExpect(
							response.get(GroupMemberListResponseDto.Fields.groupMembers)
									.get("length()").isEquals(totalMemberCount)
					)
					.andExpectAll(
							response.get(
											GroupMemberListResponseDto.Fields.groupMembers + "[0].memberId")
									.isEquals(loginMember.getId()),
							response.get(
											GroupMemberListResponseDto.Fields.groupMembers + "[0].role")
									.isEquals(GroupRole.OWNER.name())
					);
		}
	}
}
