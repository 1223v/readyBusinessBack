package com.readyvery.readyverydemo.src.ceo;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readyvery.readyverydemo.security.jwt.dto.CustomUserDetails;
import com.readyvery.readyverydemo.src.ceo.dto.CeoAuthRes;
import com.readyvery.readyverydemo.src.ceo.dto.CeoInfoRes;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CeoController {

	private final CeoService ceoServiceImpl;

	@GetMapping("/jwt-test")
	public String jwtTest() {
		return "jwtTest 요청 성공";
	}

	/**
	 * 사용자 인증 체크
	 * 인증체크 후 사용자 정보를 반환합니다.
	 * DB의 조회 없이 반환
	 * @param userDetails
	 * @return
	 */
	@GetMapping("/auth")
	public CeoAuthRes userAuth(@AuthenticationPrincipal CustomUserDetails userDetails) {
		// 서비스 계층을 호출하여 사용자 정보를 조회합니다.
		return ceoServiceImpl.getCeoAuthByCustomUserDetails(userDetails);
	}

	/**
	 * 사용자 정보 조회
	 */
	@GetMapping("/user/info")
	public CeoInfoRes userInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return ceoServiceImpl.getCeoInfoById(userDetails.getId());
	}

	/**
	 * 사용자 정보 조회
	 * CustomUserDetails의 내부 구현체인 UserDetails를 사용하여도 사용자 정보를 조회가능
	 * 인증체크 후 사용자 정보를 반환
	 * @param userDetails
	 * @return
	 */
	@GetMapping("/user/detail/info")
	public CustomUserDetails userDetail(@AuthenticationPrincipal CustomUserDetails userDetails) {
		return userDetails;
	}

	/**
	 * 사용자 로그아웃
	 */
	@GetMapping("/user/logout")
	public boolean logout(@AuthenticationPrincipal CustomUserDetails userDetails, HttpServletResponse response) {

		ceoServiceImpl.removeRefreshTokenInDB(userDetails.getId(), response);
		return true;
	}

	/**
	 * Access 토큰 재발급
	 *
	 * @return
	 */
	@GetMapping("/refresh/token")
	public boolean refreshEndpoint() {
		return true;
	}

}