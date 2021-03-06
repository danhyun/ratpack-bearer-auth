package st.ratpack.auth.handler

import com.google.common.collect.ImmutableSet
import spock.lang.Specification
import spock.lang.Unroll
import st.ratpack.auth.OAuthToken

import static ratpack.groovy.test.handling.GroovyRequestFixture.handle

class TokenScopeFilterHandlerSpec extends Specification {

	@Unroll
	def "For #scopes a token with #tokenScopes calledNext: #calledNext"() {
		given:
		OAuthToken oauthToken = new OAuthToken()
		oauthToken.setScopes(ImmutableSet.copyOf(tokenScopes))

		when:
		def result = handle(new TokenScopeFilterHandler((String[])scopes.toArray())) {
			registry.add(oauthToken)
		}


		then:
		result.calledNext == calledNext

		where:
		scopes              | tokenScopes           || calledNext
		['mobile']          | []                    || false
		['mobile', 'coool'] | ['fire']              || false
		['service']         | ['service']           || true
		['service']         | ['service', 'mobile'] || true
	}

}
