package com.example.academey.config.security

import com.example.academey.domain.user.Role
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
class WebSecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
    private val customAccessDeniedHandler: CustomAccessDeniedHandler
) : WebSecurityConfigurerAdapter() {
    @Throws(java.lang.Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
            .csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable처리.
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증할것이므로 세션필요없으므로 생성안함.
            .and()
            .exceptionHandling()
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler)
            .and()
            .authorizeRequests() // 다음 리퀘스트에 대한 사용권한 체크
            .antMatchers("/api/admin/**").hasRole(Role.ADMIN.key)
            .antMatchers("/api/user/me/**").hasRole(Role.USER.key)
            .anyRequest().permitAll() // 그외 나머지 요청은 누구나 접근 가능
            .and()
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            ) // jwt token 필터를 id/password 인증 필터 전에 넣어라.
    }
}
